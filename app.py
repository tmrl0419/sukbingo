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

from flask import Flask,request, url_for, redirect
import crawling
import DBsetting
import json

app = Flask(__name__)

@app.route("/")
def hello():
    return "hello World!"

@app.route("/main")
def main():
    return "main Page"

@app.route("/regist", methods = ['POST'])    # 회원가입.
def regist():
    user = request.form['user']                     # 아이디 중복 체크 넣어야함
    password = request.form['password']
    name = request.form['name']
    result = DBsetting.user_regist(user,password,name)
    jsonresult = {
        'result' : result
    }
    jsonstring = json.dumps(jsonresult)
    return jsonstring


@app.route("/login", methods = ['POST'])    # 로그인.
def login():
    user = request.form['user']
    password = request.form['password']
    userid = DBsetting.user_login(user,password)
    jsonresult = {
        'id' : userid
    }
    jsonString = json.dumps(jsonresult)
    print (jsonString)
    return jsonString


@app.route("/add-ingredient", methods = ['PUT'])    # 재료 추가
def add_ingredient():
    userid = request.form['user']
    ingredientid = request.form['ingredientid']
    result = DBsetting.insert_inventory(userid,ingredientid)
    jsonresult = {
        'result' : result
    }
    jsonstring = json.dumps(jsonresult)
    return jsonstring


@app.route("/delete-ingredient", methods = ['DELETE'])    # 재료 추가
def delete_ingredient():
    userid = request.form['user']
    ingredientid = request.form['ingredientid']
    result = DBsetting.delete_inventory(userid,ingredientid)
    jsonresult = {
        'result' : result
    }
    jsonstring = json.dumps(jsonresult)
    return jsonstring


@app.route("/get-userinfo", methods = ['POST'])    # 재료 추가
def get_userinfo():
    user = request.form['user']
    result = DBsetting.get_userInfo(user)
    jsonresult = {
        'result' : result
    }
    jsonstring = json.dumps(jsonresult)
    return jsonstring


@app.route("/get-foodinfo", methods = ['POST'])    # 재료 추가
def get_foodinfo():
    food = request.form['food']
    result = DBsetting.get_foodInfo(food)
    jsonresult = {
        'result' : result
    }
    jsonstring = json.dumps(jsonresult)
    return jsonstring


@app.route("/search", methods = ['GET'])    # 검색
def search():
    userid = request.args.get('user')
    result = DBsetting.search(userid)
    jsonresult = {
        'result' : result
    }
    jsonstring = json.dumps(jsonresult)
    return jsonstring


if __name__ =="__main__":
    app.run(host='0.0.0.0',port = 8000,debug='True')

#각 페이지마다 행동들 정해주기,
#맨 처음 실행될때 initialize -> make_food_table, make_Relation_Food_ingredient
