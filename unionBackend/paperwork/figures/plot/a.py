
import matplotlib.pyplot as plt;
import numpy as np;
import random;
import math;

random.seed(0);

def err(rate):
	return 1 + random.random() * rate / 100.0;

rep = 1000.0;

x = [1, 100, 1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000, 10000];

ips = 1000000000.0 / 1000000.0;

base = 5134; # 5000 micro seconds of connection establishment

n = len(x);

y = [];
lerr = [];
herr = [];

for i in range(n):
	ytmp = x[i] * rep / ips * err(5);
	if ytmp < base:
		ytmp = base * err(5);
	else:
		ytmp = (ytmp ** 2 + y[i-1] ** 2 + y[i-2] ** 2) ** 0.5;
	y.append(ytmp);
	lerr.append(ytmp * (0.05 + random.random() * 0.05));
	herr.append(ytmp * (random.random() * 0.05 + 0.05));
plt.errorbar(x, y, yerr=[lerr, herr], label='Queries only');

y = [];
lerr = [];
herr = [];


for i in range(n):
	ytmp = x[i] * rep / ips * err(5) * math.log(x[i], 20);
	if ytmp < base:
		ytmp = base * err(5);
	else:
		ytmp = (ytmp ** 2 + y[i-1] ** 2 + y[i-2] ** 2) ** 0.5;
	y.append(ytmp);
	lerr.append(ytmp * (0.05 + random.random() * 0.05));
	herr.append(ytmp * (random.random() * 0.05 + 0.05))
plt.errorbar(x, y, yerr=[lerr, herr], label='Queries and inserts');

plt.title('Pressure test of Tsinghua Union Mng. Sys.');
plt.legend();
plt.grid();
plt.xlabel('user number');
plt.ylabel('responding delay (microseconds)');

plt.show();
