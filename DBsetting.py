#-------------------------------------------------------------------------------
# Name:        module1
# Purpose:
#
# Author:      them0
#
# Created:     01-05-2018
# Copyright:   (c) them0 2018
# Licence:     <your licence>
#-------------------------------------------------------------------------------

import crawling
import pymysql
from operator import eq

conn = pymysql.connect(host='localhost', user = 'root', password = '456123', db='test', charset='utf8')
curs = conn.cursor();

def no_continuous(s):                                                                               # list 중복 없애는 함수.
    r = []
    for i in range(len(s)):
        if i == 0:
            r.append(s[i])
        elif s[i] != s[i-1]:
            r.append(s[i])
    return r

def make_food_table():                                                                              # crawling 으로 디비에 자료 넣는 함수.
    curs.execute('DELETE FROM food')                                                                 # 자료넣기전에 이전 값 초기화.
    curs.execute('DELETE FROM foodingredient')
    f = open('url.txt','r')                                                                         # url.txt에서 url값들을 불러옴.
    list = []
    name = []
    temp = []
    n = 1
    SQL_food = 'INSERT INTO food (id,name, url, img_url) VALUES (%s , %s , %s, %s)'                 # food 정보 insert sql 문.
    SQL_foodingredient = 'INSERT INTO foodingredient (foodid,ingredientid) VALUES ( %s , %s )'      # food와 ingredient 관계 table.
    curs.execute('SELECT * FROM ingredient')
    result = curs.fetchall()

    for line in f:                                                                                  # usl을 list에 하나씩 넣기
        list.append((line.strip('\n')))
    f.close()

    for url in list:
        name_url = crawling.spider_name(url)                                                        # 이름 crawling
        img = crawling.spider_img(url)                                                              # 이미지 crawling
        curs.execute(SQL_food, ( n , name_url , url, img))                                          #food테이블에 id, name, url , img 넣기.
        conn.commit()
        temp = crawling.spider_ingredients(url)                                                     # 재료 crawling
        for ingre in temp:                                                                          #foodingredient 테이블 만들기.
            for row_data in result:
                if( eq( row_data[1] , ingre ) ):                                                    # 크롤링한 재료와 ingredient에 있는 재료와 매칭이 된다면 추가.
                    curs.execute(SQL_foodingredient, ( n , row_data[0]))
                    conn.commit()
        n = n+1


def user_regist(user,password,name):
    SQL_chk = 'SELECT * FROM user WHERE user = %s'
    curs.execute(SQL_chk, (user))
    n = curs.fetchone()
    if (n==None):
        SQL_regist = 'INSERT INTO user (user,password,name) VALUES (%s,%s,%s)'
        curs.execute(SQL_regist, (user, password,name))
        conn.commit()
        return True
    return False


def user_login(user,password):
    SQL_user = 'SELECT * FROM user WHERE user = %s'
    curs.execute(SQL_user, (user))
    n = curs.fetchone()
    if( n == None):                                                                                 # 없는 id라면 return -200
        return -200
    elif password == n[1]:                                                                          # 일치한다면 return id값
        return n[2]
    else:                                                                                           # 비밀번호가 다르다면 return -100
        return -100

def get_userInfo(userid):
    SQL_user = 'SELECT * FROM user WHERE user = %s'
    curs.execute(SQL_user, (userid))
    result = curs.fetchone()
    return result

def show_menu():
    SQL_menu = 'SELECT name FROM food'
    curs.execute(SQL_menu)
    menu = curs.fetchall()
    result = []
    for food in menu:
        result.append(food[0])
    print(result)

def show_ingredientAll():
    SQL_ingredient = 'SELECT name FROM ingredient'
    curs.execute(SQL_ingredient)
    ingredient = curs.fetchall()
    result = []
    for food in ingredient:
        result.append(food[0])
    print(result)

def get_foodInfo(foodid):
    SQL_food = 'SELECT * FROM food WHERE name = %s'
    SQL_foodingredient = 'SELECT ingredientid FROM foodingredient WHERE foodid = %s'
    SQL_ingredient = 'SELECT name FROM ingredient WHERE id = %s'
    curs.execute(SQL_food, (foodid))
    foodinfo = curs.fetchone()
    curs.execute(SQL_foodingredient, (foodinfo[0]))
    list_ingreidnet = curs.fetchall()
    result = []
    for info in foodinfo:
        result.append(info)
    for i in list_ingreidnet:
        curs.execute(SQL_ingredient, (i))
        list = curs.fetchone()
        result.append(list[0])
    return result




def insert_inventory( userid, ingredientid ):
    SQL_useringredient = 'INSERT INTO useringredient ( userid,ingredientid ) VALUES ( %s, %s )'
    curs.execute(SQL_useringredient, (userid, ingredientid) )
    conn.commit()
    return True



def delete_inventory(userid,ingredientid):
    SQL_useringredient = 'DELETE FROM useringredient WHERE userid = %s and ingredientid = %s'
    curs.execute(SQL_useringredient, (userid,ingredientid))
    conn.commit()
    return True

def search(user):

    result = []
    useringredient =[]

    SQL_useringredinet = 'SELECT * FROM useringredient WHERE userid = %s'       #DB문 선언
    SQL_useringredinet2 = 'SELECT * FROM foodingredient WHERE foodid = %s'

    curs.execute( SQL_useringredinet, (user))
    userresult = curs.fetchall()                #가진 요리 재료만 리스트로
    for i in userresult:
        useringredient.append(i[1])             # user의 재료 useringredient list만들기.

    curs.execute('select count(*) from food')   #갯수 수하기
    n = curs.fetchone()
    k = n[0]

    for i in range(1,k+1):

        ingredient = []
        temp = []

        curs.execute(SQL_useringredinet2, i)
        ingredient = curs.fetchall()

        for item in ingredient:
            temp.append(item[1])

        ingredient = no_continuous(temp)
        useringredient = no_continuous(useringredient)
        newlist = [ item for item in ingredient if item in useringredient]
        if(eq(newlist,ingredient)):
            result.append(i)                    #요리 id 추가 하기.
    return result


if __name__ == '__main__':
    #make_food_table()
    #print(user_regist('98712','123123','asdf'))
    #print(get_userInfo(123))
    #print(get_foodInfo('오므라이스'))
    #show_menu()
    show_ingredientAll()
