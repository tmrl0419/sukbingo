import requests
import re
import string
from bs4 import BeautifulSoup

def spider_ingredients(url):
    source_code = requests.get(url)
    plain_text = source_code.text
    soup = BeautifulSoup(plain_text,'html.parser')
    string = soup.find(property="og:description")
    parse = re.sub('[(|-|=|.|#|/|?|:|$|}|)|[|]|||를', '', str(string))
    list = (str(parse)).split(" ")
    return list

def spider_name(url):
    source_code = requests.get(url)
    plain_text = source_code.text
    soup = BeautifulSoup(plain_text,'html.parser')
    string2 = soup.findAll(property="og:title")
    temp = str(string2)
    temp = temp.split('"')
    temp = str(temp[1])
    temp = temp.split()
    return str(temp[0])

def spider_img(url):
    source_code = requests.get(url)
    plain_text = source_code.text
    soup = BeautifulSoup(plain_text,'html.parser')
    string = soup.find(property = "og:image" )
    temp = str(string)
    temp = temp.split('"')
    temp = str(temp[1])
    temp = temp.split()
    return str(temp[0])

if __name__ == '__main__':
    #print("test!!")
    list = spider_ingredients('http://terms.naver.com/entry.nhn?docId=1989304&cid=42785&categoryId=44169')
    print(list)
    #spider_name('http://terms.naver.com/entry.nhn?docId=1989868&cid=42785&categoryId=44169')
    #str = spider_img('https://terms.naver.com/entry.nhn?docId=1989309&cid=48163&categoryId=48201')
    #print(str)