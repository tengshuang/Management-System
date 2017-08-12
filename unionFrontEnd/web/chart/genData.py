# -*- coding: utf-8 -*-


import numpy as np;
from math import floor;
import json;

from faker import Factory;

fake = Factory.create('zh_CN');

xlim = 20;
# y = ['分工会1', '分工会2', '分工会3', '分工会4', '分工会5']
res = [];
for i in range(20):
	ind = {};
	cnt = [0] * xlim;
	rnd = np.random.normal(np.random.randint(19, size=1)[0], 5.0, 100);
	for j in rnd:
		j = int(j);
		if j < xlim and j >= 0:
			cnt[j] += 1;
	tot = 0;
	fmt = [];
	for j in range(xlim):
		fmt.append([j, cnt[j]]);
		tot += cnt[j];
	ind['articles'] = fmt;
	ind['total'] = tot;
	ind['name'] = fake.name();
	res.append(ind);

fo = open("content.json", 'w');
fo.write(json.dumps(res, indent=4));
fo.close();
