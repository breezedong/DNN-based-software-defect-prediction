# encoding:utf-8
import tensorflow as tf
import input_data as id
def add_layer(inputs, in_size, out_size, activation_function=None):
    Weights = tf.Variable(tf.truncated_normal([in_size, out_size]))
    biases = tf.Variable(tf.zeros([1, out_size]) + 0.1)
    Wx_plus_b = tf.matmul(inputs, Weights) + biases
    if activation_function is None:
        outputs = Wx_plus_b
    else:
        outputs = activation_function(Wx_plus_b)
    return outputs

x_train,y_train,x_test,y_test = id.readData()
xs = tf.placeholder(tf.float32, [None, 39])
ys = tf.placeholder(tf.float32, [None, 2])
# add hidden layer 输入值是 xs，在隐藏层有 10 个神经元
l1 = add_layer(xs, 39, 1000, activation_function=tf.nn.relu)
l2 = add_layer(l1, 1000, 1000, activation_function=tf.nn.relu)
l3 = add_layer(l2, 1000, 1000, activation_function=tf.nn.relu)
l4 = add_layer(l3, 1000, 1000, activation_function=tf.nn.relu)
l5 = add_layer(l4, 1000, 1000, activation_function=tf.nn.relu)
l6 = add_layer(l5, 1000, 1000, activation_function=tf.nn.relu)
l7 = add_layer(l6, 1000, 1000, activation_function=tf.nn.relu)
l8 = add_layer(l7, 1000, 1000, activation_function=tf.nn.relu)
l9 = add_layer(l8, 1000, 1000, activation_function=tf.nn.relu)
l10 = add_layer(l9, 1000, 1000, activation_function=tf.nn.relu)
l11 = add_layer(l10, 1000, 1000, activation_function=tf.nn.relu)
l12 = add_layer(l11, 1000, 1000, activation_function=tf.nn.relu)
l13 = add_layer(l12, 1000, 1000, activation_function=tf.nn.relu)
l14 = add_layer(l13, 1000, 1000, activation_function=tf.nn.relu)
l15 = add_layer(l14, 1000, 1000, activation_function=tf.nn.relu)
l16 = add_layer(l15, 1000, 1000, activation_function=tf.nn.relu)
l17 = add_layer(l16, 1000, 1000, activation_function=tf.nn.relu)
# add output layer 输入值是隐藏层 l1，在预测层输出 1 个结果
prediction = add_layer(l6, 1000, 2, activation_function=None)
loss = tf.reduce_mean(tf.nn.softmax_cross_entropy_with_logits(prediction, ys))
train_step = tf.train.AdamOptimizer(1e-4).minimize(loss)
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