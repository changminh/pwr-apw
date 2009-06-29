%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 

% Rule name: rule_1 
% If-complexes: 
rule_1___if_1(_, _, 'petallength', _, _) :- 
	'petallength' > 4.7
.


% if-clause: 
rule_1___ifClause(_, _, 'petallength', _, _) :- 
	rule_1___if_1(_, _, 'petallength', _, _)
.


% Then-complexes: 
rule_1___then_1(_, _, _, _, 'class') :- 
	member('class', ['Iris-virginica'])
.


% then-clause: 
rule_1___thenClause(_, _, _, _, 'class') :- 
	rule_1___then_1(_, _, _, _, 'class')
.


% Whole rule: 
rule_1(_, _, 'petallength', _, 'class') :- 
	rule_1___thenClause(_, _, _, _, 'class') ; \+ (rule_1___ifClause(_, _, 'petallength', _, _))
.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 

% Rule name: rule_3 
% If-complexes: 
rule_3___if_1('sepal length', 'sepalwidth', 'petallength', _, _) :- 
	'sepal length' =< 5.899,
	'sepalwidth' > 2.6,
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

% Rule name: rule_2 
% If-complexes: 
rule_2___if_1(_, 'sepalwidth', 'petallength', 'petalwidth', _) :- 
	'sepalwidth' =< 3.899,
	'petallength' =< 4.7989999999999995,
	'petalwidth' > 0.3
.


% if-clause: 
rule_2___ifClause(_, 'sepalwidth', 'petallength', 'petalwidth', _) :- 
	rule_2___if_1(_, 'sepalwidth', 'petallength', 'petalwidth', _)
.


% Then-complexes: 
rule_2___then_1(_, _, _, _, 'class') :- 
	member('class', ['Iris-versicolor'])
.


% then-clause: 
rule_2___thenClause(_, _, _, _, 'class') :- 
	rule_2___then_1(_, _, _, _, 'class')
.


% Whole rule: 
rule_2(_, 'sepalwidth', 'petallength', 'petalwidth', 'class') :- 
	rule_2___thenClause(_, _, _, _, 'class') ; \+ (rule_2___ifClause(_, 'sepalwidth', 'petallength', 'petalwidth', _))
.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 


