# encoding:utf-8
import numpy as np

def readData():
    project = 'Wikipedia'
    x_data1 = np.loadtxt("/home/zsd/paper/data/marxrt/"+project+"/train1.txt", dtype=np.int)
    x_data2 = np.loadtxt("/home/zsd/paper/data/marxrt/"+project+"/train2.txt", dtype=np.int)
    x_data3 = np.loadtxt("/home/zsd/paper/data/marxrt/"+project+"/train3.txt", dtype=np.int)
    x_data4 = np.loadtxt("/home/zsd/paper/data/marxrt/"+project+"/train4.txt", dtype=np.int)
    y_data1 = np.loadtxt("/home/zsd/paper/data/marxrt/"+project+"/labeldnn1.txt", dtype=np.int)
    y_data2 = np.loadtxt("/home/zsd/paper/data/marxrt/"+project+"/labeldnn2.txt", dtype=np.int)
    y_data3 = np.loadtxt("/home/zsd/paper/data/marxrt/"+project+"/labeldnn3.txt", dtype=np.int)
    y_data4 = np.loadtxt("/home/zsd/paper/data/marxrt/"+project+"/labeldnn4.txt", dtype=np.int)
    x_train = np.concatenate((x_data1, x_data2, x_data3, x_data4))
    y_train = np.concatenate((y_data1, y_data2, y_data3, y_data4))
    x_test = np.loadtxt("/home/zsd/paper/data/marxrt/"+project+"/train5.txt", dtype=np.int)
    y_test = np.loadtxt("/home/zsd/paper/data/marxrt/"+project+"/labeldnn5.txt", dtype=np.int)
    return x_train,y_train,x_test,y_test