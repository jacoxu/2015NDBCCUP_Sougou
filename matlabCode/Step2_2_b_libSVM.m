clear;clc;
isSelfEvalute = 0; %1:从标注数据中抽出1/5进行验证，0:直接对测试数据进行标注
isKernel =  1; % 1:SVM是否使用Gaussian高斯核，0: SVM采用线性Linear
hasLoadedData = 0; %0: 还没有加载过数据，1: 已经加载过数据了
needPostProcess = 0; %是否进行后处理
if ~hasLoadedData
    for i =1:10
        if((i~=7)&&(i~=8))
            load(['./../feaSet/Step2_1_b_',num2str(i),'_labeledDataFea_nonSyn.txt']);
            if i>3
                load(['./../feaSet/Step2_1_b_',num2str(i),'_labeledDataFea_Syn.txt']);    
            end
        end
    end
    load('./../feaSet/Step2_3_a_labeledData.txt');
    %% 标注数据特征融合
    labeled_fea = [Step2_1_b_1_labeledDataFea_nonSyn, Step2_1_b_2_labeledDataFea_nonSyn,...
        Step2_1_b_3_labeledDataFea_nonSyn,Step2_1_b_4_labeledDataFea_nonSyn,...
        Step2_1_b_5_labeledDataFea_nonSyn, Step2_1_b_6_labeledDataFea_nonSyn,...
        Step2_1_b_9_labeledDataFea_nonSyn', Step2_1_b_10_labeledDataFea_nonSyn',...
        Step2_1_b_4_labeledDataFea_Syn, Step2_1_b_5_labeledDataFea_Syn,...
        Step2_1_b_6_labeledDataFea_Syn, Step2_1_b_9_labeledDataFea_Syn',...
        Step2_1_b_10_labeledDataFea_Syn'
        ];
    isMatchNumEng_labeledData = Step2_3_a_labeledData;
    for i =1:10
        if((i~=7)&&(i~=8))
            load(['./../feaSet/Step2_1_b_',num2str(i),'_unlabeledDataFea_nonSyn.txt']);
            if i>3
                load(['./../feaSet/Step2_1_b_',num2str(i),'_unlabeledDataFea_Syn.txt']);    
            end
        end
    end

    load('./../feaSet/Step2_3_a_unlabeledData.txt');
    %% 测试数据特征融合
    unlabeled_fea = [Step2_1_b_1_unlabeledDataFea_nonSyn, Step2_1_b_2_unlabeledDataFea_nonSyn,...
        Step2_1_b_3_unlabeledDataFea_nonSyn,Step2_1_b_4_unlabeledDataFea_nonSyn,...
        Step2_1_b_5_unlabeledDataFea_nonSyn, Step2_1_b_6_unlabeledDataFea_nonSyn,...
        Step2_1_b_9_unlabeledDataFea_nonSyn', Step2_1_b_10_unlabeledDataFea_nonSyn',...
        Step2_1_b_4_unlabeledDataFea_Syn, Step2_1_b_5_unlabeledDataFea_Syn,...
        Step2_1_b_6_unlabeledDataFea_Syn, Step2_1_b_9_unlabeledDataFea_Syn',...
        Step2_1_b_10_unlabeledDataFea_Syn'
        ];
    isMatchNumEng_unlabeledData = Step2_3_a_unlabeledData;
    % 读取标签数据
    load('./../feaSet/Step2_2_a_tags.txt')
    train_labels = Step2_2_a_tags;

    clear Step2_*;

    save './../feaSet/all_Fea.mat' labeled_fea unlabeled_fea train_labels isMatchNumEng_*

else
    load('./../feaSet/all_Fea.mat')
end
iniIndx = 4;
if isSelfEvalute
    unlabeled_fea = labeled_fea(iniIndx:5:22591,:);
    test_labels = train_labels(iniIndx:5:22591,:);
    isMatchNumEng_unlabeledData = isMatchNumEng_labeledData(iniIndx:5:22591,:);
    labeled_fea(iniIndx:5:22591,:) = [];
    train_labels(iniIndx:5:22591,:) = [];
    isMatchNumEng_labeledData(iniIndx:5:22591,:) = [];
end

% %% 进行SVM模型训练和预测
% labeled_fea = sparse(labeled_fea);
% unlabeled_fea = sparse(unlabeled_fea);
% tic
% y = zeros(length(unlabeled_fea(:,1)),1);
% if ~isKernel
%     disp('start train model ...')
%     model = train(train_labels, labeled_fea, '-q');
%     disp('start predict test data ...')
%     predict_label = predict(y,unlabeled_fea,model);
% else
%     disp('start train model ...')
%     model = svmtrain(train_labels,labeled_fea,['-q -c 1 -g ',int2str(length(labeled_fea(1,: )))]);
%     disp('start predict test data ...')
%     predict_label = svmpredict(y,unlabeled_fea,model);
% end
% toc
% if isSelfEvalute
%     ACC = length(find(predict_label == test_labels))/length(test_labels)*100;
%     disp(['[SVM-RBF] Accuracy is ',num2str(ACC)])
%     mSquErr = sum((predict_label-test_labels).^2)/length(test_labels);
%     disp(['[SVM-RBF] Mean Square Error is ',num2str(mSquErr)])
% else
%    save './../predictSVM_label.txt' predict_label -ascii
% end
% if needPostProcess
%     predict_label(find(predict_label(find(isMatchNumEng_unlabeledData==0))==2)) =1;
%     predict_label(find(predict_label(find(isMatchNumEng_unlabeledData==0))==3)) =1;
%     predict_label(find(predict_label(find(isMatchNumEng_unlabeledData==0))==1)) =0;
%     if isSelfEvalute
%         ACC = length(find(predict_label == test_labels))/length(test_labels)*100;
%         disp(['PostPre: Accuracy is ',num2str(ACC)])
%         mSquErr = sum((predict_label-test_labels).^2)/length(test_labels);
%         disp(['Mean Square Error is ',num2str(mSquErr)])
%     else
%        save './../predictSVM_label_posPre.txt' predict_label -ascii
%     end
% end

%% 进行随机森林分类器（Random Forest）训练和预测
labeled_fea = full(labeled_fea);
unlabeled_fea = full(unlabeled_fea);
nTree = 1000;
tic
disp('start train model ...')
model = TreeBagger(nTree, labeled_fea, train_labels);
disp('start predict test data ...')
[predict_label,predict_scores] = predict(model, unlabeled_fea);
predict_label = str2num(cell2mat(predict_label));
predict_label_p = predict_scores(:,1)*0+predict_scores(:,2)*1+predict_scores(:,3)*2+predict_scores(:,4)*3;
save RFLabel.mat predict_label predict_label_p;
toc
if isSelfEvalute
    ACC = length(find(predict_label == test_labels))/length(test_labels)*100;
    disp(['[Random Forest] Accuracy is ',num2str(ACC)])
    mSquErr = sum((predict_label - test_labels).^2)/length(test_labels);
    disp(['[Random Forest] Mean Square Error is ',num2str(mSquErr)])
else
   save './../predictRandomForest_label.txt' predict_label -ascii
end
if needPostProcess
    predict_label(find(predict_label(find(isMatchNumEng_unlabeledData==0))==2)) =1;
    predict_label(find(predict_label(find(isMatchNumEng_unlabeledData==0))==3)) =1;
    predict_label(find(predict_label(find(isMatchNumEng_unlabeledData==0))==1)) =0;
    if isSelfEvalute
        ACC = length(find(predict_label == test_labels))/length(test_labels)*100;
        disp(['PostPre: Accuracy is ',num2str(ACC)])
        mSquErr = sum((predict_label - test_labels).^2)/length(test_labels);
        disp(['Mean Square Error is ',num2str(mSquErr)])
    else
       save './../predictRandomForest_label_posPre.txt' predict_label -ascii
    end
end

if isSelfEvalute
    ACC = length(find(predict_label_p == test_labels))/length(test_labels)*100;
    disp(['[Random Forest_P] Accuracy is ',num2str(ACC)])
    mSquErr = sum((predict_label_p - test_labels).^2)/length(test_labels);
    disp(['[Random Forest_P] Mean Square Error is ',num2str(mSquErr)])
else
   save './../predictRandomForest_labelP.txt' predict_label_p -ascii
end
if needPostProcess
    predict_label_p(find(predict_label_p(find(isMatchNumEng_unlabeledData==0))==2)) =1;
    predict_label_p(find(predict_label_p(find(isMatchNumEng_unlabeledData==0))==3)) =1;
    predict_label_p(find(predict_label_p(find(isMatchNumEng_unlabeledData==0))==1)) =0;
    if isSelfEvalute
        ACC = length(find(predict_label_p == test_labels))/length(test_labels)*100;
        disp(['PostPre: Accuracy is ',num2str(ACC)])
        mSquErr = sum((predict_label_p - test_labels).^2)/length(test_labels);
        disp(['Mean Square Error is ',num2str(mSquErr)])
    else
       save './../predictRandomForest_labelP_posPre.txt' predict_label_p -ascii
    end
end

% %% 进行逻辑回归(多项式MultiNomial logistic Regression)训练和预测
% labeled_fea = full(labeled_fea);
% unlabeled_fea = full(unlabeled_fea);
% tic
% disp('start train model ...')
% train_labels = train_labels+1;
% model = mnrfit(labeled_fea, train_labels);
% disp('start predict test data ...')
% predict_scores = mnrval(model, unlabeled_fea);
% train_labels = train_labels -1;
% predict_label_p = predict_scores(:,1)*0+predict_scores(:,2)*1+predict_scores(:,3)*2+predict_scores(:,4)*3;
% toc
% if isSelfEvalute
%     ACC = length(find(predict_label_p == test_labels))/length(test_labels)*100;
%     disp(['[MultiNomial LR] Accuracy is ',num2str(ACC)])
%     mSquErr = sum((predict_label_p - test_labels).^2)/length(test_labels);
%     disp(['[MultiNomial LR] Mean Square Error is ',num2str(mSquErr)])
% else
%    save './../predictRandomForest_labelP.txt' predict_label_p -ascii
% end
% if needPostProcess
%     predict_label_p(find(isMatchNumEng_unlabeledData==0)) = predict_label_p(find(isMatchNumEng_unlabeledData==0))-1;
%     if isSelfEvalute
%         mSquErr = sum((predict_label_p - test_labels).^2)/length(test_labels);
%         disp(['Mean Square Error is ',num2str(mSquErr)])
%     else
%        save './../predictMultiNomialLR_labelP_posPre.txt' predict_label_p -ascii
%     end
% end
save('./../tmpClassification.mat');
load('./../tmpClassification.mat');


