from selenium import webdriver
from selenium.common.exceptions import NoSuchElementException
from selenium.webdriver.firefox.options import Options
from selenium.webdriver.common.keys import Keys
import time
import xlsxwriter

sitebase = "https://www.al.sp.gov.br/"
base = f"{sitebase}/deputado/lista/"

options = webdriver.ChromeOptions()
options.binary_location = '/opt/google/chrome/google-chrome'
# All the arguments added for chromium to work on selenium
options.add_argument("--no-sandbox")  # This make Chromium reachable
options.add_argument("--no-default-browser-check")  # Overrides default choices
options.add_argument("--no-first-run")
options.add_argument("--disable-default-apps")
driver = webdriver.Chrome('/home/edisonribeiroaraujo/.local/bin/chromedriver', chrome_options=options)
## base de entrada
driver.get(base)

##xpath=//div[@id='conteudo']/div/div/form/table[2]/tbody/tr/td/a
##xpath=//div[@id='conteudo']/div/div/form/table[2]/tbody/tr[3]/td/a

contagem = 1
driver.get(f"{base}")
time.sleep(2.0)
parlamentares = []

## Vai para a página com listagem completa dos deputados.
while contagem < 9999:

    try:
        element_link = driver.find_element_by_xpath(
            f"//div[@id='conteudo']/div/div/form/table[2]/tbody/tr[{contagem}]/td/a")
        element_partido = driver.find_element_by_xpath(
            f"//div[@id='conteudo']/div/div/form/table[2]/tbody/tr[{contagem}]/td[4]")
    except NoSuchElementException:
        print('Fim da lista')
        break;

    href = element_link.get_property("href")
    id = href.split("=")[1]
    nome = element_link.text
    partido = element_partido.text
    parlamentares.append({"id": id, "nome": nome, "href": href, "partido": partido})
    print(f"{id}\t{nome}\t{href}\t{partido}")
    contagem += 1

## Vai página a página de cada deputado e complementa os dados da página anterior
for parlamentar in parlamentares:
    driver.get(parlamentar["href"])
    time.sleep(2)
    dados = driver.find_elements_by_xpath("//input")
    parlamentar["telefone"] = dados[5].get_property("value").strip()
    parlamentar["email"] = dados[8].get_property("value").strip()

## Coloca em um excel.
workbook = xlsxwriter.Workbook("camara_dep_sp.xlsx")
workbooksheet = workbook.add_worksheet()

row = 0
col = 0

### Monta o cabeçalho padrão para a planilha.
# id	órgão	nome	partido	titular	Estado	telefone	e-mail	tratamento	sexo
for i in ["id", "órgão", "nome", "partido", "titular", "Estado", "telefone", "e-mail", "tratamento", "sexo"]:
    workbooksheet.write(row, col, i)
    col += 1

#Grava linha a linha ... parlamentar a parlamentar.
for parlamentar in parlamentares:
    row += 1

    col = 0
    workbooksheet.write(row, col, parlamentar['id'])
    col += 1

    workbooksheet.write(row, col, "Câmara Deputados do Estado de SP")
    col += 1

    workbooksheet.write(row, col, parlamentar["nome"])
    col += 1

    workbooksheet.write(row, col, parlamentar["partido"])
    col += 1

    workbooksheet.write(row, col, "T")
    col += 1

    workbooksheet.write(row, col, "SP")
    col += 1

    workbooksheet.write(row, col, parlamentar["telefone"])
    col += 1

    workbooksheet.write(row, col, parlamentar["email"])
    col += 1

    feminino = parlamentar["nome"].split()[0].endswith("a") or parlamentar["nome"].split()[0].endswith("e")

    if feminino:
        workbooksheet.write(row, col, "Vossa Excelência Deputada Estadual")
    else:
        workbooksheet.write(row, col, "Vossa Excelência Deputado Estadual")
    col += 1

    if feminino:
        workbooksheet.write(row, col, "F")
    else:
        workbooksheet.write(row, col, "M")
    col += 1

workbook.close()

print("Fim do processamento")

driver.close()

