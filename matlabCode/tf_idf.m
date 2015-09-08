function [testQuery_TFIDF,testTitle_TFIDF,trainQuery_TFIDF,trainTitle_TFIDF] ...
    = tf_idf(vsmBW_TestQuery, vsmBW_TestTitle, vsmBW_TrainQuery, vsmBW_TrainTitle)
% function [trainX, testX] = tf_idf(trainTF, testTF)
% TF-IDF weighting
% ([1+log(tf)]*log[N/df])
allTexts = [vsmBW_TestQuery; vsmBW_TestTitle; vsmBW_TrainQuery; vsmBW_TrainTitle];
%抽取输入VSM向量空间模型的维数，即得到文档数和term数
[n,m] = size(allTexts);  % the number of (training) documents and terms
%统计训练数据VSM中Term的文档频次
df = sum(allTexts>0);  % (training) document frequency
% %只抽取Term频次大于0的数据，即过滤训练文档中从未出现过的词
% d = sum(df>0); % the number of dimensions, i.e., terms occurred in (training) documents
% %对文档频次df进行列排序，得到排序后的结果dfY和序列号次序dfI
% [dfY, dfI] = sort(df, 2, 'descend');
% %从训练集的VSM中过滤掉未出现词
% vsmBW_TrainQuery =vsmBW_TrainQuery(:,dfI(1:d));
% vsmBW_TrainTitle =vsmBW_TrainTitle(:,dfI(1:d));
% 
% %相应着也从测试集中过滤掉对应的词
% vsmBW_TestQuery =vsmBW_TestQuery(:,dfI(1:d));
% vsmBW_TestTitle =vsmBW_TestTitle(:,dfI(1:d));
%计算Term的idf向量
idf = log(n./df);
%生成IDF稀疏矩阵，用于后面的矩阵运算
IDF = sparse(1:m,1:m,idf);
%从训练集VSM中找出非零元素，分别为，行号，列号和元素值
[vsmBW_TrainQueryI,vsmBW_TrainQueryJ,vsmBW_TrainQueryV] = find(vsmBW_TrainQuery);
[vsmBW_TrainTitleI,vsmBW_TrainTitleJ,vsmBW_TrainTitleV] = find(vsmBW_TrainTitle);
%计算了训练数据集TF-IDF前半段后，生成稀疏矩阵
trainQueryLogTF = sparse(vsmBW_TrainQueryI,vsmBW_TrainQueryJ,1+log(vsmBW_TrainQueryV),...
    size(vsmBW_TrainQuery,1),size(vsmBW_TrainQuery,2));
trainTitleLogTF = sparse(vsmBW_TrainTitleI,vsmBW_TrainTitleJ,1+log(vsmBW_TrainTitleV),...
    size(vsmBW_TrainTitle,1),size(vsmBW_TrainTitle,2));

%从测试集VSM中找出非零元素，分别为，行号，列号和元素值
[vsmBW_TestQueryI,vsmBW_TestQueryJ,vsmBW_TestQueryV] = find(vsmBW_TestQuery);
[vsmBW_TestTitleI,vsmBW_TestTitleJ,vsmBW_TestTitleV] = find(vsmBW_TestTitle);
%计算了测试数据集TF-IDF前半段后，生成稀疏矩阵
testQueryLogTF = sparse(vsmBW_TestQueryI,vsmBW_TestQueryJ,1+log(vsmBW_TestQueryV),...
    size(vsmBW_TestQuery,1),size(vsmBW_TestQuery,2));
testTitleLogTF = sparse(vsmBW_TestTitleI,vsmBW_TestTitleJ,1+log(vsmBW_TestTitleV),...
    size(vsmBW_TestTitle,1),size(vsmBW_TestTitle,2));

%生成完整的TF-IDF矩阵
testQuery_TFIDF = testQueryLogTF*IDF;
testTitle_TFIDF = testTitleLogTF*IDF;
trainQuery_TFIDF = trainQueryLogTF*IDF;
trainTitle_TFIDF = trainTitleLogTF*IDF;
end
