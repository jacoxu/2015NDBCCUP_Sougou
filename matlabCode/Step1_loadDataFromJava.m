clear;clc;
load('./../../data/jacoxu/vsmOfall.txt');
vsmOfall = sparse(vsmOfall);
load('./../../data/jacoxu/all_label.txt');
load('./../../data/jacoxu/testIdx.txt');
load('./../../data/jacoxu/trainIdx.txt');
load('./../../data/jacoxu/test_FeaSet0.txt');
load('./../../data/jacoxu/train_FeaSet0.txt');
save('./../../data/jacoxu/2015Semantic.mat');
