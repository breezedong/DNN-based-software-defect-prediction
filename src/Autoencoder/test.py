import util
from bean import autoencoder, nn
import numpy as np

x = np.array([[1,1,1,3,1,1,3,1,1,2,1,1]])
x = util.sigmod(x)
print x
nodes = [12, 6]
ae = util.aebuilder(nodes)
ae = util.aetrain(ae, x, 6000)
nodescomplete = np.array([12, 6, 12])
aecomplete = nn(nodescomplete)

for i in range(len(nodescomplete) - 2):
    aecomplete.W[i] = ae.encoders[i].W[0]

aecomplete = util.nntrain(aecomplete, x, x, 3000)
print  aecomplete.values[2]

y = np.array([[1,3,7,4,10,12,8,5,10,7,11,9,7,4,6,2]])
y = util.sigmod(y)
print y
nodes = [16, 12]
ae = util.aebuilder(nodes)
ae = util.aetrain(ae, y, 6000)

nodescomplete = np.array([16, 12, 16])
aecomplete = nn(nodescomplete)

for i in range(len(nodescomplete) - 2):
    aecomplete.W[i] = ae.encoders[i].W[0]

aecomplete = util.nntrain(aecomplete, y, y, 3000)
print  aecomplete.values[2]
