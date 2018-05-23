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

def no_continuous(s):
    # 함수를 완성하세요
    r=[]    #정답을 저장할 빈공간의 list 생성
    for i in range(len(s)): #i를 번지수로 돌릴것이며 범위는 s의 갯수만큼
        if i == 0:          #초기값은 무조건 0이기 때문에 중복될수가 없음
            r.append(s[i])  #0은 바로 추가시켜줍니다
        elif s[i] != s[i-1]:#현재 주소와 그전의 주소가 같지 않을경우
            r.append(s[i])  #지금 값을 추가해줍니다
    return r

def make_food_table():
    curs.execute('DELETE FROM food')
    curs.execute('DELETE FROM foodingredient')
    f = open('url.txt','r')
    list = []
    name = []
    temp = []
    n = 1
    SQL_food = 'INSERT INTO food (id,name, url, img_url) VALUES (%s , %s , %s, %s)'
    SQL_foodingredient = 'INSERT INTO foodingredient (foodid,ingredientid) VALUES ( %s , %s )'
    curs.execute('SELECT * FROM ingredient')
    result = curs.fetchall()

    for line in f:
        list.append((line.strip('\n')))
    f.close()

    for url in list:
        name_url = crawling.spider_name(url)
        img = crawling.spider_img(url)
        curs.execute(SQL_food, ( n , name_url , url, img))                            #food테이블에 id, name, url 넣기
        conn.commit()
        temp = crawling.spider_ingredients(url)
        for ingre in temp:                                                  #foodingredient 테이블 만들기
            for row_data in result:
                if( eq( row_data[1] , ingre ) ):
                    #print( n, row_data[0])
                    curs.execute(SQL_foodingredient, ( n , row_data[0]))
                    conn.commit()
        n = n+1


def user_regist(user,password,name):

    SQL_regist = 'INSERT INTO user (user,password,name) VALUES (%s,%s,%s)'
    curs.execute(SQL_regist, (user, password,name))
    conn.commit()
    return True


def user_login(user,password):
    SQL_useringredinet = 'SELECT * FROM user WHERE user = %s'
    curs.execute(SQL_useringredinet, (user))
    n = curs.fetchone()
    if password == n[1]:
        return n[2]
    else:
        return -100


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
    print(result)
    return result


if __name__ == '__main__':
    #make_food_table()
    #user_regist('432','432432','minseokkim')
    #print(user_login('123','123123'))
    insert_inventory(1,14)
    insert_inventory(1,15)
    insert_inventory(1,11)
    insert_inventory(1,29)
    insert_inventory(1,28)
    insert_inventory(1,27)
    insert_inventory(1,26)
    insert_inventory(1,25)
    insert_inventory(1,24)
    insert_inventory(1,23)
    insert_inventory(1,22)
    insert_inventory(1,21)
    #delete_inventory(1,11)
    #delete_inventory(1,14)
    #delete_inventory(1,15)
    #search(1)