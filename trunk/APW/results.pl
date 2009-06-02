%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 

% Rule name: rule_1 
% If-complexes: 
rule_1___if_1(_, _, Atr_3, Atr_4, _) :- 
	Atr_3 > 4.8,
	Atr_4 > 1.7
.


% if-clause: 
rule_1___ifClause(_, _, Atr_3, Atr_4, _) :- 
	rule_1___if_1(_, _, Atr_3, Atr_4, _)
.


% Then-complexes: 
rule_1___then_1(_, _, _, _, Atr_5) :- 
	member(Atr_5, ['Iris-virginica'])
.


% then-clause: 
rule_1___thenClause(_, _, _, _, Atr_5) :- 
	rule_1___then_1(_, _, _, _, Atr_5)
.


% Whole rule: 
rule_1(_, _, Atr_3, Atr_4, Atr_5) :- 
	rule_1___thenClause(_, _, _, _, Atr_5) ; \+ (rule_1___ifClause(_, _, Atr_3, Atr_4, _))
.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 

% Rule name: rule_2 
% If-complexes: 
rule_2___if_1(Atr_1, Atr_2, Atr_3, _, _) :- 
	Atr_1 =< 6.399,
	Atr_2 > 3.0,
	Atr_3 =< 4.499
.


% if-clause: 
rule_2___ifClause(Atr_1, Atr_2, Atr_3, _, _) :- 
	rule_2___if_1(Atr_1, Atr_2, Atr_3, _, _)
.


% Then-complexes: 
rule_2___then_1(_, _, _, _, Atr_5) :- 
	member(Atr_5, ['Iris-setosa'])
.


% then-clause: 
rule_2___thenClause(_, _, _, _, Atr_5) :- 
	rule_2___then_1(_, _, _, _, Atr_5)
.


% Whole rule: 
rule_2(Atr_1, Atr_2, Atr_3, _, Atr_5) :- 
	rule_2___thenClause(_, _, _, _, Atr_5) ; \+ (rule_2___ifClause(Atr_1, Atr_2, Atr_3, _, _))
.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 

% Rule name: rule_3 
% If-complexes: 
rule_3___if_1(Atr_1, Atr_2, Atr_3, Atr_4, _) :- 
	Atr_1 > 4.9,
	Atr_2 =< 3.899,
	1.6 < Atr_3, Atr_3 =< 4.999,
	0.5 < Atr_4, Atr_4 =< 1.7990000000000002
.


% if-clause: 
rule_3___ifClause(Atr_1, Atr_2, Atr_3, Atr_4, _) :- 
	rule_3___if_1(Atr_1, Atr_2, Atr_3, Atr_4, _)
.


% Then-complexes: 
rule_3___then_1(_, _, _, _, Atr_5) :- 
	member(Atr_5, ['Iris-versicolor'])
.


% then-clause: 
rule_3___thenClause(_, _, _, _, Atr_5) :- 
	rule_3___then_1(_, _, _, _, Atr_5)
.


% Whole rule: 
rule_3(Atr_1, Atr_2, Atr_3, Atr_4, Atr_5) :- 
	rule_3___thenClause(_, _, _, _, Atr_5) ; \+ (rule_3___ifClause(Atr_1, Atr_2, Atr_3, Atr_4, _))
.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 

% Rule name: rule_4 
% If-complexes: 
rule_4___if_1(_, _, _, Atr_4, _) :- 
	Atr_4 =< 0.999
.


% if-clause: 
rule_4___ifClause(_, _, _, Atr_4, _) :- 
	rule_4___if_1(_, _, _, Atr_4, _)
.


% Then-complexes: 
rule_4___then_1(_, _, _, _, Atr_5) :- 
	member(Atr_5, ['Iris-setosa'])
.


% then-clause: 
rule_4___thenClause(_, _, _, _, Atr_5) :- 
	rule_4___then_1(_, _, _, _, Atr_5)
.


% Whole rule: 
rule_4(_, _, _, Atr_4, Atr_5) :- 
	rule_4___thenClause(_, _, _, _, Atr_5) ; \+ (rule_4___ifClause(_, _, _, Atr_4, _))
.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 

% Rule name: rule_5 
% If-complexes: 
rule_5___if_1(Atr_1, Atr_2, _, _, _) :- 
	4.8 < Atr_1, Atr_1 =< 5.7989999999999995,
	Atr_2 =< 2.499
.


% if-clause: 
rule_5___ifClause(Atr_1, Atr_2, _, _, _) :- 
	rule_5___if_1(Atr_1, Atr_2, _, _, _)
.


% Then-complexes: 
rule_5___then_1(_, _, _, _, Atr_5) :- 
	member(Atr_5, ['Iris-versicolor'])
.


% then-clause: 
rule_5___thenClause(_, _, _, _, Atr_5) :- 
	rule_5___then_1(_, _, _, _, Atr_5)
.


% Whole rule: 
rule_5(Atr_1, Atr_2, _, _, Atr_5) :- 
	rule_5___thenClause(_, _, _, _, Atr_5) ; \+ (rule_5___ifClause(Atr_1, Atr_2, _, _, _))
.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 

% Rule name: rule_6 
% If-complexes: 
rule_6___if_1(Atr_1, Atr_2, Atr_3, _, _) :- 
	6.0 < Atr_1, Atr_1 =< 6.699,
	2.5 < Atr_2, Atr_2 =< 3.099,
	Atr_3 > 4.7
.


% if-clause: 
rule_6___ifClause(Atr_1, Atr_2, Atr_3, _, _) :- 
	rule_6___if_1(Atr_1, Atr_2, Atr_3, _, _)
.


% Then-complexes: 
rule_6___then_1(_, _, _, _, Atr_5) :- 
	member(Atr_5, ['Iris-virginica'])
.


% then-clause: 
rule_6___thenClause(_, _, _, _, Atr_5) :- 
	rule_6___then_1(_, _, _, _, Atr_5)
.


% Whole rule: 
rule_6(Atr_1, Atr_2, Atr_3, _, Atr_5) :- 
	rule_6___thenClause(_, _, _, _, Atr_5) ; \+ (rule_6___ifClause(Atr_1, Atr_2, Atr_3, _, _))
.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 

% Rule name: rule_7 
% If-complexes: 
rule_7___if_1(Atr_1, Atr_2, Atr_3, Atr_4, _) :- 
	5.1 < Atr_1, Atr_1 =< 7.199,
	2.5 < Atr_2, Atr_2 =< 2.799,
	1.5 < Atr_3, Atr_3 =< 5.199,
	0.4 < Atr_4, Atr_4 =< 1.7990000000000002
.


% if-clause: 
rule_7___ifClause(Atr_1, Atr_2, Atr_3, Atr_4, _) :- 
	rule_7___if_1(Atr_1, Atr_2, Atr_3, Atr_4, _)
.


% Then-complexes: 
rule_7___then_1(_, _, _, _, Atr_5) :- 
	member(Atr_5, ['Iris-versicolor'])
.


% then-clause: 
rule_7___thenClause(_, _, _, _, Atr_5) :- 
	rule_7___then_1(_, _, _, _, Atr_5)
.


% Whole rule: 
rule_7(Atr_1, Atr_2, Atr_3, Atr_4, Atr_5) :- 
	rule_7___thenClause(_, _, _, _, Atr_5) ; \+ (rule_7___ifClause(Atr_1, Atr_2, Atr_3, Atr_4, _))
.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 

% Rule name: rule_8 
% If-complexes: 
rule_8___if_1(Atr_1, Atr_2, _, Atr_4, _) :- 
	Atr_1 > 6.7,
	2.7 < Atr_2, Atr_2 =< 3.1990000000000003,
	Atr_4 > 1.5
.


% if-clause: 
rule_8___ifClause(Atr_1, Atr_2, _, Atr_4, _) :- 
	rule_8___if_1(Atr_1, Atr_2, _, Atr_4, _)
.


% Then-complexes: 
rule_8___then_1(_, _, _, _, Atr_5) :- 
	member(Atr_5, ['Iris-virginica'])
.


% then-clause: 
rule_8___thenClause(_, _, _, _, Atr_5) :- 
	rule_8___then_1(_, _, _, _, Atr_5)
.


% Whole rule: 
rule_8(Atr_1, Atr_2, _, Atr_4, Atr_5) :- 
	rule_8___thenClause(_, _, _, _, Atr_5) ; \+ (rule_8___ifClause(Atr_1, Atr_2, _, Atr_4, _))
.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 

% Rule name: rule_9 
% If-complexes: 
rule_9___if_1(Atr_1, Atr_2, Atr_3, _, _) :- 
	Atr_1 =< 6.199,
	Atr_2 =< 2.599,
	Atr_3 > 4.0
.


% if-clause: 
rule_9___ifClause(Atr_1, Atr_2, Atr_3, _, _) :- 
	rule_9___if_1(Atr_1, Atr_2, Atr_3, _, _)
.


% Then-complexes: 
rule_9___then_1(_, _, _, _, Atr_5) :- 
	member(Atr_5, ['Iris-virginica'])
.


% then-clause: 
rule_9___thenClause(_, _, _, _, Atr_5) :- 
	rule_9___then_1(_, _, _, _, Atr_5)
.


% Whole rule: 
rule_9(Atr_1, Atr_2, Atr_3, _, Atr_5) :- 
	rule_9___thenClause(_, _, _, _, Atr_5) ; \+ (rule_9___ifClause(Atr_1, Atr_2, Atr_3, _, _))
.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 

% Rule name: rule_10 
% If-complexes: 
rule_10___if_1(Atr_1, Atr_2, Atr_3, Atr_4, _) :- 
	5.0 < Atr_1, Atr_1 =< 7.199,
	2.6 < Atr_2, Atr_2 =< 3.399,
	4.9 < Atr_3, Atr_3 =< 5.098999999999999,
	Atr_4 > 0.5
.


% if-clause: 
rule_10___ifClause(Atr_1, Atr_2, Atr_3, Atr_4, _) :- 
	rule_10___if_1(Atr_1, Atr_2, Atr_3, Atr_4, _)
.


% Then-complexes: 
rule_10___then_1(_, _, _, _, Atr_5) :- 
	member(Atr_5, ['Iris-versicolor'])
.


% then-clause: 
rule_10___thenClause(_, _, _, _, Atr_5) :- 
	rule_10___then_1(_, _, _, _, Atr_5)
.


% Whole rule: 
rule_10(Atr_1, Atr_2, Atr_3, Atr_4, Atr_5) :- 
	rule_10___thenClause(_, _, _, _, Atr_5) ; \+ (rule_10___ifClause(Atr_1, Atr_2, Atr_3, Atr_4, _))
.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 

% Rule name: rule_11 
% If-complexes: 
rule_11___if_1(Atr_1, Atr_2, Atr_3, Atr_4, _) :- 
	5.7 < Atr_1, Atr_1 =< 6.098999999999999,
	2.5 < Atr_2, Atr_2 =< 3.099,
	1.4 < Atr_3, Atr_3 =< 5.098999999999999,
	Atr_4 > 1.5
.


% if-clause: 
rule_11___ifClause(Atr_1, Atr_2, Atr_3, Atr_4, _) :- 
	rule_11___if_1(Atr_1, Atr_2, Atr_3, Atr_4, _)
.


% Then-complexes: 
rule_11___then_1(_, _, _, _, Atr_5) :- 
	member(Atr_5, ['Iris-virginica'])
.


% then-clause: 
rule_11___thenClause(_, _, _, _, Atr_5) :- 
	rule_11___then_1(_, _, _, _, Atr_5)
.


% Whole rule: 
rule_11(Atr_1, Atr_2, Atr_3, Atr_4, Atr_5) :- 
	rule_11___thenClause(_, _, _, _, Atr_5) ; \+ (rule_11___ifClause(Atr_1, Atr_2, Atr_3, Atr_4, _))
.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 

% Rule name: rule_12 
% If-complexes: 
rule_12___if_1(Atr_1, Atr_2, Atr_3, Atr_4, _) :- 
	4.9 < Atr_1, Atr_1 =< 6.098999999999999,
	3.0 < Atr_2, Atr_2 =< 3.399,
	1.4 < Atr_3, Atr_3 =< 4.899,
	Atr_4 > 0.5
.


% if-clause: 
rule_12___ifClause(Atr_1, Atr_2, Atr_3, Atr_4, _) :- 
	rule_12___if_1(Atr_1, Atr_2, Atr_3, Atr_4, _)
.


% Then-complexes: 
rule_12___then_1(_, _, _, _, Atr_5) :- 
	member(Atr_5, ['Iris-versicolor'])
.


% then-clause: 
rule_12___thenClause(_, _, _, _, Atr_5) :- 
	rule_12___then_1(_, _, _, _, Atr_5)
.


% Whole rule: 
rule_12(Atr_1, Atr_2, Atr_3, Atr_4, Atr_5) :- 
	rule_12___thenClause(_, _, _, _, Atr_5) ; \+ (rule_12___ifClause(Atr_1, Atr_2, Atr_3, Atr_4, _))
.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 


