# encoding:utf-8
import tensorflow as tf
import numpy as np

def add_layer(inputs, in_size, out_size, activation_function=None):
    Weights = tf.Variable(tf.truncated_normal([in_size, out_size]))
    biases = tf.Variable(tf.zeros([1, out_size]) + 0.1)
    Wx_plus_b = tf.matmul(inputs, Weights) + biases
    if activation_function is None:
        outputs = Wx_plus_b
    else:
        outputs = activation_function(Wx_plus_b)
    return outputs


project1 = 'AnkiDroid'
project2 = 'BankDroid'
x_data1 = np.loadtxt("/home/zsd/paper/data/marxrt/"+project1+"/train1.txt", dtype=np.int)
x_data2 = np.loadtxt("/home/zsd/paper/data/marxrt/"+project1+"/train2.txt", dtype=np.int)
x_data3 = np.loadtxt("/home/zsd/paper/data/marxrt/"+project1+"/train3.txt", dtype=np.int)
x_data4 = np.loadtxt("/home/zsd/paper/data/marxrt/"+project1+"/train4.txt", dtype=np.int)
x_data5 = np.loadtxt("/home/zsd/paper/data/marxrt/"+project1+"/train5.txt", dtype=np.int)
y_data1 = np.loadtxt("/home/zsd/paper/data/marxrt/"+project1+"/labeldnn1.txt", dtype=np.int)
y_data2 = np.loadtxt("/home/zsd/paper/data/marxrt/"+project1+"/labeldnn2.txt", dtype=np.int)
y_data3 = np.loadtxt("/home/zsd/paper/data/marxrt/"+project1+"/labeldnn3.txt", dtype=np.int)
y_data4 = np.loadtxt("/home/zsd/paper/data/marxrt/"+project1+"/labeldnn4.txt", dtype=np.int)
y_data5 = np.loadtxt("/home/zsd/paper/data/marxrt/"+project1+"/labeldnn5.txt", dtype=np.int)
x_test1 = np.loadtxt("/home/zsd/paper/data/marxrt/"+project2+"/train1.txt", dtype=np.int)
x_test2 = np.loadtxt("/home/zsd/paper/data/marxrt/"+project2+"/train2.txt", dtype=np.int)
x_test3 = np.loadtxt("/home/zsd/paper/data/marxrt/"+project2+"/train3.txt", dtype=np.int)
x_test4 = np.loadtxt("/home/zsd/paper/data/marxrt/"+project2+"/train4.txt", dtype=np.int)
x_test5 = np.loadtxt("/home/zsd/paper/data/marxrt/"+project2+"/train5.txt", dtype=np.int)
y_test1 = np.loadtxt("/home/zsd/paper/data/marxrt/"+project2+"/labeldnn1.txt", dtype=np.int)
y_test2 = np.loadtxt("/home/zsd/paper/data/marxrt/"+project2+"/labeldnn2.txt", dtype=np.int)
y_test3 = np.loadtxt("/home/zsd/paper/data/marxrt/"+project2+"/labeldnn3.txt", dtype=np.int)
y_test4 = np.loadtxt("/home/zsd/paper/data/marxrt/"+project2+"/labeldnn4.txt", dtype=np.int)
y_test5 = np.loadtxt("/home/zsd/paper/data/marxrt/"+project2+"/labeldnn5.txt", dtype=np.int)
x_train = np.concatenate((x_data1, x_data2, x_data3, x_data4, x_data5))
y_train = np.concatenate((y_data1, y_data2, y_data3, y_data4, y_data5))
x_test = np.concatenate((x_test1, x_test2, x_test3, x_test4, x_test5))
y_test = np.concatenate((y_test1, y_test2, y_test3, y_test4, y_test5))


xs = tf.placeholder(tf.float32, [None, 39])
ys = tf.placeholder(tf.float32, [None, 2])

# add hidden layer 输入值是 xs，在隐藏层有 10 个神经元
l1 = add_layer(xs, 39, 150, activation_function=tf.nn.relu)
l2 = add_layer(l1, 150, 150, activation_function=tf.nn.relu)
l3 = add_layer(l2, 150, 150, activation_function=tf.nn.relu)
l4 = add_layer(l3, 150, 150, activation_function=tf.nn.relu)
# add output layer 输入值是隐藏层 l1，在预测层输出 1 个结果
prediction = add_layer(l4, 150, 2, activation_function=None)
loss = tf.reduce_mean(tf.nn.softmax_cross_entropy_with_logits(prediction, ys))
train_step = tf.train.AdamOptimizer(1e-4).minimize(loss)
# ACCURACY
p = tf.argmax(prediction, 1)
q = tf.argmax(ys, 1)
label = tf.cast(q, dtype=tf.float32)
pre = tf.cast(p, dtype=tf.float32)
auc, update_op_auc, FPR, TPR = tf.contrib.metrics.streaming_auc(predictions=pre, labels=label)
acc = tf.contrib.metrics.accuracy(predictions=tf.cast(p, dtype=tf.int64), labels=tf.cast(q, dtype=tf.int64))
sess = tf.Session()
sess.run(tf.initialize_local_variables())
sess.run(tf.initialize_all_variables())
for i in range(10000):
    sess.run(train_step, feed_dict={xs: x_train, ys: y_train})
    if i % 50 == 0:
        print(sess.run(loss, feed_dict={xs: x_train, ys: y_train}))
        print("Accuracy:", acc.eval(session=sess, feed_dict={xs: x_test, ys: y_test}))
        sess.run(tf.initialize_local_variables())
        update_op_auc.eval(session=sess, feed_dict={xs: x_test, ys: y_test})
        print("AUC:", sess.run(auc, feed_dict={xs: x_test, ys: y_test})[0])
        print("FPR:", FPR.eval(session=sess, feed_dict={xs: x_test, ys: y_test})[20])
        print("TPR:", TPR.eval(session=sess, feed_dict={xs: x_test, ys: y_test})[20])
        print