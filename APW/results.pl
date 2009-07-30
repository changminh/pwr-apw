%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 

% Rule name: rule_1 
% If-complexes: 
rule_1___if_1(_, _, 'petallength', 'petalwidth', _) :- 
	1.7 < 'petallength', 'petallength' =< 4.999,
	'petalwidth' > 0.4
.


% if-clause: 
rule_1___ifClause(_, _, 'petallength', 'petalwidth', _) :- 
	rule_1___if_1(_, _, 'petallength', 'petalwidth', _)
.


% Then-complexes: 
rule_1___then_1(_, _, _, _, 'class') :- 
	member('class', ['Iris-versicolor'])
.


% then-clause: 
rule_1___thenClause(_, _, _, _, 'class') :- 
	rule_1___then_1(_, _, _, _, 'class')
.


% Whole rule: 
rule_1(_, _, 'petallength', 'petalwidth', 'class') :- 
	rule_1___thenClause(_, _, _, _, 'class') ; \+ (rule_1___ifClause(_, _, 'petallength', 'petalwidth', _))
.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 

% Rule name: rule_2 
% If-complexes: 
rule_2___if_1('sepal length', _, 'petallength', _, _) :- 
	'sepal length' > 6.0,
	'petallength' > 4.9
.


% if-clause: 
rule_2___ifClause('sepal length', _, 'petallength', _, _) :- 
	rule_2___if_1('sepal length', _, 'petallength', _, _)
.


% Then-complexes: 
rule_2___then_1(_, _, _, _, 'class') :- 
	member('class', ['Iris-virginica'])
.


% then-clause: 
rule_2___thenClause(_, _, _, _, 'class') :- 
	rule_2___then_1(_, _, _, _, 'class')
.


% Whole rule: 
rule_2('sepal length', _, 'petallength', _, 'class') :- 
	rule_2___thenClause(_, _, _, _, 'class') ; \+ (rule_2___ifClause('sepal length', _, 'petallength', _, _))
.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 

% Rule name: rule_3 
% If-complexes: 
rule_3___if_1('sepal length', 'sepalwidth', 'petallength', _, _) :- 
	'sepal length' =< 5.999,
	'sepalwidth' > 2.8,
	'petallength' =< 4.499
.


% if-clause: 
rule_3___ifClause('sepal length', 'sepalwidth', 'petallength', _, _) :- 
	rule_3___if_1('sepal length', 'sepalwidth', 'petallength', _, _)
.


% Then-complexes: 
rule_3___then_1(_, _, _, _, 'class') :- 
	member('class', ['Iris-setosa'])
.


% then-clause: 
rule_3___thenClause(_, _, _, _, 'class') :- 
	rule_3___then_1(_, _, _, _, 'class')
.


% Whole rule: 
rule_3('sepal length', 'sepalwidth', 'petallength', _, 'class') :- 
	rule_3___thenClause(_, _, _, _, 'class') ; \+ (rule_3___ifClause('sepal length', 'sepalwidth', 'petallength', _, _))
.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 

% Rule name: rule_4 
% If-complexes: 
rule_4___if_1('sepal length', 'sepalwidth', _, 'petalwidth', _) :- 
	4.9 < 'sepal length', 'sepal length' =< 6.2989999999999995,
	'sepalwidth' =< 2.799,
	'petalwidth' > 1.6
.


% if-clause: 
rule_4___ifClause('sepal length', 'sepalwidth', _, 'petalwidth', _) :- 
	rule_4___if_1('sepal length', 'sepalwidth', _, 'petalwidth', _)
.


% Then-complexes: 
rule_4___then_1(_, _, _, _, 'class') :- 
	member('class', ['Iris-virginica'])
.


% then-clause: 
rule_4___thenClause(_, _, _, _, 'class') :- 
	rule_4___then_1(_, _, _, _, 'class')
.


% Whole rule: 
rule_4('sepal length', 'sepalwidth', _, 'petalwidth', 'class') :- 
	rule_4___thenClause(_, _, _, _, 'class') ; \+ (rule_4___ifClause('sepal length', 'sepalwidth', _, 'petalwidth', _))
.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 

% Rule name: rule_5 
% If-complexes: 
rule_5___if_1('sepal length', 'sepalwidth', 'petallength', 'petalwidth', _) :- 
	4.9 < 'sepal length', 'sepal length' =< 6.899,
	2.2 < 'sepalwidth', 'sepalwidth' =< 3.299,
	'petallength' =< 5.598999999999999,
	1.5 < 'petalwidth', 'petalwidth' =< 1.899
.


% if-clause: 
rule_5___ifClause('sepal length', 'sepalwidth', 'petallength', 'petalwidth', _) :- 
	rule_5___if_1('sepal length', 'sepalwidth', 'petallength', 'petalwidth', _)
.


% Then-complexes: 
rule_5___then_1(_, _, _, _, 'class') :- 
	member('class', ['Iris-versicolor'])
.


% then-clause: 
rule_5___thenClause(_, _, _, _, 'class') :- 
	rule_5___then_1(_, _, _, _, 'class')
.


% Whole rule: 
rule_5('sepal length', 'sepalwidth', 'petallength', 'petalwidth', 'class') :- 
	rule_5___thenClause(_, _, _, _, 'class') ; \+ (rule_5___ifClause('sepal length', 'sepalwidth', 'petallength', 'petalwidth', _))
.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 

% Rule name: rule_6 
% If-complexes: 
rule_6___if_1('sepal length', 'sepalwidth', 'petallength', 'petalwidth', _) :- 
	5.7 < 'sepal length', 'sepal length' =< 6.2989999999999995,
	'sepalwidth' =< 2.599,
	'petallength' =< 5.098999999999999,
	'petalwidth' > 1.0
.


% if-clause: 
rule_6___ifClause('sepal length', 'sepalwidth', 'petallength', 'petalwidth', _) :- 
	rule_6___if_1('sepal length', 'sepalwidth', 'petallength', 'petalwidth', _)
.


% Then-complexes: 
rule_6___then_1(_, _, _, _, 'class') :- 
	member('class', ['Iris-virginica'])
.


% then-clause: 
rule_6___thenClause(_, _, _, _, 'class') :- 
	rule_6___then_1(_, _, _, _, 'class')
.


% Whole rule: 
rule_6('sepal length', 'sepalwidth', 'petallength', 'petalwidth', 'class') :- 
	rule_6___thenClause(_, _, _, _, 'class') ; \+ (rule_6___ifClause('sepal length', 'sepalwidth', 'petallength', 'petalwidth', _))
.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 


