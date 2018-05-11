#-------------------------------------------------------------------------------
# Name:        module1
# Purpose:
#
# Author:      them0
#
# Created:     30-04-2018
# Copyright:   (c) them0 2018
# Licence:     <your licence>
#-------------------------------------------------------------------------------

from flask import Flask
import crawling
import DBsetting

app = Flask(__name__)

@app.route("/")
def hello():
    return "hello World!"


@app.route('/main')
def main():
    return 'main Page'

@app.route('/regist')
def regist():
    return 'regist'

@app.search('/search')
def search():
    list = []
    list = DBsetting.search(user)


if __name__ =="__main__":

    app.run(host='0.0.0.0',debug='True')
#각 페이지마다 행동들 정해주기,
#맨 처음 실행될때 initialize -> make_food_table, make_Relation_Food_ingredient