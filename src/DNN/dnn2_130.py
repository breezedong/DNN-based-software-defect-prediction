# encoding:utf-8
import tensorflow as tf
import numpy as np
import input_data as id

# from sklearn.cross_validation import train_test_split

def add_layer(inputs, in_size, out_size, activation_function=None):
    Weights = tf.Variable(tf.truncated_normal([in_size, out_size]))
    biases = tf.Variable(tf.zeros([1, out_size]) + 0.1)
    Wx_plus_b = tf.matmul(inputs, Weights) + biases
    if activation_function is None:
        outputs = Wx_plus_b
    else:
        outputs = activation_function(Wx_plus_b)
    return outputs


'''
x_data = np.linspace(-1,1,300)[:, np.newaxis]
noise = np.random.normal(0, 0.05, x_data.shape)
y_data = np.square(x_data) - 0.5 + noise
'''

x_train,y_train,x_test,y_test = id.readData()

# train_test_split
# x_train, x_test, y_train, y_test = train_test_split(x_data, y_data, test_size=0.3, random_state=0)

xs = tf.placeholder(tf.float32, [None, 39])
ys = tf.placeholder(tf.float32, [None, 2])

# add hidden layer 输入值是 xs，在隐藏层有 10 个神经元
l1 = add_layer(xs, 39, 130, activation_function=tf.nn.relu)
l2 = add_layer(l1, 130, 130, activation_function=tf.nn.relu)
l3 = add_layer(l2, 130, 130, activation_function=tf.nn.relu)
# add output layer 输入值是隐藏层 l1，在预测层输出 1 个结果
prediction = add_layer(l3, 130, 2, activation_function=None)

'''
l1 = add_layer(xs, 1, 10, activation_function=tf.nn.relu)
prediction = add_layer(l1, 10, 1, activation_function=None)
'''
'''
loss = tf.reduce_mean(tf.reduce_sum(tf.square(ys - prediction),
                     reduction_indices=[1]))
'''

loss = tf.reduce_mean(tf.nn.softmax_cross_entropy_with_logits(prediction, ys))
# train_step = tf.train.GradientDescentOptimizer(0.01).minimize(loss)
train_step = tf.train.AdamOptimizer(1e-4).minimize(loss)
# ACCURACY
p = tf.argmax(prediction, 1)
q = tf.argmax(ys, 1)
# correct_prediction = tf.equal(p, q)
# accuracy = tf.reduce_mean(tf.cast(correct_prediction, tf.float32))
# test
# accuracy = evalt(tf.argmax(prediction,1), tf.argmax(ys,1))
# FPR,TPR
# la = tf.constant(y_test2)
# label = tf.cast(la,tf.bool)
label = tf.cast(q, dtype=tf.float32)
pre = tf.cast(p, dtype=tf.float32)
auc, update_op_auc, FPR, TPR = tf.contrib.metrics.streaming_auc(predictions=pre, labels=label)
acc = tf.contrib.metrics.accuracy(predictions=tf.cast(p, dtype=tf.int64), labels=tf.cast(q, dtype=tf.int64))
# compute thresholds
'''kepsilon = 1e-7  # to account for floating point imprecisions
num_thresholds = 200
thresholds = [(i + 1) * 1.0 / (num_thresholds - 1)for i in range(num_thresholds-2)]
thresholds = [0.0 - kepsilon] + thresholds + [1.0 + kepsilon]
'''
# dd = (true_positives, false_negatives, true_negatives, false_positives,_,_,_,_) = tf.contrib.metrics._tp_fn_tn_fp(predictions=pre, labels=label, thresholds=thresholds)
sess = tf.Session()
sess.run(tf.initialize_local_variables())
sess.run(tf.initialize_all_variables())
for i in range(10000):
    sess.run(train_step, feed_dict={xs: x_train, ys: y_train})
    if i % 50 == 0:
        print(sess.run(loss, feed_dict={xs: x_train, ys: y_train}))
        # print(update_op_acc.eval(session=sess, feed_dict={xs: x_test, ys: y_test}))
        print("Accuracy:", acc.eval(session=sess, feed_dict={xs: x_test, ys: y_test}))
        # print(accuracy.eval(session=sess, feed_dict={xs: x_test, ys: y_test}))
        sess.run(tf.initialize_local_variables())
        update_op_auc.eval(session=sess, feed_dict={xs: x_test, ys: y_test})
        #print("AUC:", auc.eval(session=sess, feed_dict={xs: x_test, ys: y_test}))
        print("AUC:", sess.run(auc, feed_dict={xs: x_test, ys: y_test})[0])
        print("FPR:", FPR.eval(session=sess, feed_dict={xs: x_test, ys: y_test})[20])
        print("TPR:", TPR.eval(session=sess, feed_dict={xs: x_test, ys: y_test})[20])
        # print(sess.run(dd, feed_dict={xs: x_test, ys: y_test}))
        print