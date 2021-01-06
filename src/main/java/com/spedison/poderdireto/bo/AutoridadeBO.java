package com.spedison.poderdireto.bo;

import com.spedison.poderdireto.model.AppConfiguration;
import com.spedison.poderdireto.model.Autoridade;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Component
@Log4j2
@NoArgsConstructor
public class AutoridadeBO {

    @Autowired
    private AppConfiguration appConfiguration;

    @Autowired
    private DadosDeConfiguracaoDeEnvioBO dadosDeConfiguracaoDeEnvioBO;

    int pulaLinhas = 1;

    private static List<Autoridade> listaAutoridades = new LinkedList<>();

    public List<Autoridade> carregaLista() {
        return carregaLista(false);
    }

    public List<Autoridade> carregaLista(String orgao, boolean test, int inicio, int quantidade) {
        String email = dadosDeConfiguracaoDeEnvioBO.carregaDadosEnvio().getEmail();
        List<Autoridade> origem = carregaLista(orgao);

        List<Autoridade> dest = new LinkedList<>();
        origem.stream().
                skip(inicio).
                limit(test ? 1 : ((quantidade == 0 || quantidade == -1) ? origem.size() : quantidade)).
                forEach(aOrigem -> {
                    Autoridade aDestino = aOrigem.copy();
                    if (test)
                        aDestino.setEmail(email);
                    dest.add(aDestino);
                });

        return dest;
    }

    public List<Autoridade> carregaLista(String orgao) {
        List<Autoridade> la = carregaLista();
        return la.stream().filter(autoridade -> autoridade.getOrgao().equalsIgnoreCase(orgao)).collect(Collectors.toList());
    }

    public List<Autoridade> carregaLista(boolean force) {

        if (listaAutoridades.size() > 0 && force == false)
            return listaAutoridades;

        listaAutoridades.clear();

        File dirContatos = new File(appConfiguration.getLocalDirConfig());
        File[] planilhasContatos = dirContatos.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().endsWith(".xlsx");
            }
        });

        for (File file : planilhasContatos) {

            try {
                FileInputStream arquivo = new FileInputStream(file); // new File(appConfiguration.getLocalArquivoContatos()));

                XSSFWorkbook workbook = new XSSFWorkbook(arquivo);
                XSSFSheet sheetAlunos = workbook.getSheetAt(0);
                Iterator<Row> rowIterator = sheetAlunos.iterator();

                // ignore firts N lines
                for (int i = 0; i < pulaLinhas; i++) {
                    if (rowIterator.hasNext()) {
                        rowIterator.next();
                    }
                }

                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    Iterator<Cell> cellIterator = row.cellIterator();

                    Autoridade autoridade = new Autoridade();
                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        log.trace("Carregando Célula com conteúdo: " + cell.getCellType() + "Posicao :" + cell.getColumnIndex());
                        switch (cell.getColumnIndex()) {
                            case 0: // ID
                                autoridade.setId((long) cell.getNumericCellValue());
                                break;
                            case 1: // Nome do Órgão ou Lista
                                autoridade.setOrgao(cell.getStringCellValue());
                                break;
                            case 2: // Nome do Cidadão
                                autoridade.setNome(cell.getStringCellValue());
                                break;
                            case 3: // Nome do Partido
                                autoridade.setPartido(cell.getStringCellValue());
                                break;
                            case 4: // Se é titular ou não
                                autoridade.setIsTitular(cell.getStringCellValue() == "T");
                                break;
                            case 5: // Estado que representa.
                                autoridade.setEstado(cell.getStringCellValue());
                                break;
                            case 6: // Telefone
                                autoridade.setTelefone(cell.getStringCellValue());
                                break;
                            case 7: // e-mail.
                                autoridade.setEmail(cell.getStringCellValue());
                                break;
                            case 8: // Qual o pronome de tratamento que será utilizado.
                                autoridade.setTratamento(cell.getStringCellValue());
                                break;
                            case 9: // Qual o Sexo (M ou F)
                                autoridade.setSexo(cell.getStringCellValue().charAt(0));
                                break;
                        }
                    }
                    listaAutoridades.add(autoridade);
                }
                arquivo.close();

            } catch (FileNotFoundException e) {
                log.error("Arquivo não encontrado", e);
            } catch (IOException e) {
                log.error("Problemas de IO na leitura/escrita de arquivo ou rede", e);
            }
        }

        return listaAutoridades;
    }


    public List<String> listaOrgaos() {
        List<Autoridade> la = carregaLista();
        return la.stream().map(i -> i.getOrgao()).sorted().distinct().collect(Collectors.toList());
    }

    public Long contaEmailsDoOrgao(String orgao) {
        List<Autoridade> la = carregaLista();
        return la.stream().filter(i -> orgao.equals(i.getOrgao())).count();
    }
}
