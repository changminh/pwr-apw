%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 

% Rule name: regula_nr_1 
% If-complexes: 
regula_nr_1___if_1(_, _, _, Atr_4, _) :- 
	Atr_4 =< 0.96
.


% if-clause: 
regula_nr_1___ifClause(_, _, _, Atr_4, _) :- 
	regula_nr_1___if_1(_, _, _, Atr_4, _)
.


% Then-complexes: 
regula_nr_1___then_1(_, _, _, _, Atr_5) :- 
	member(Atr_5, ['Iris-setosa'])
.


% then-clause: 
regula_nr_1___thenClause(_, _, _, _, Atr_5) :- 
	regula_nr_1___then_1(_, _, _, _, Atr_5)
.


% Whole rule: 
regula_nr_1(_, _, _, Atr_4, Atr_5) :- 
	regula_nr_1___thenClause(_, _, _, _, Atr_5) ; \+ (regula_nr_1___ifClause(_, _, _, Atr_4, _))
.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 

% Rule name: rule_1 
% If-complexes: 
rule_1___if_1(Atr_1, Atr_2, Atr_3, Atr_4, _) :- 
	Atr_1 > 4.9,
	Atr_2 =< 3.399,
	1.7 < Atr_3, Atr_3 =< 4.999,
	0.2 < Atr_4, Atr_4 =< 1.7990000000000002
.


% if-clause: 
rule_1___ifClause(Atr_1, Atr_2, Atr_3, Atr_4, _) :- 
	rule_1___if_1(Atr_1, Atr_2, Atr_3, Atr_4, _)
.


% Then-complexes: 
rule_1___then_1(_, _, _, _, Atr_5) :- 
	member(Atr_5, ['Iris-versicolor'])
.


% then-clause: 
rule_1___thenClause(_, _, _, _, Atr_5) :- 
	rule_1___then_1(_, _, _, _, Atr_5)
.


% Whole rule: 
rule_1(Atr_1, Atr_2, Atr_3, Atr_4, Atr_5) :- 
	rule_1___thenClause(_, _, _, _, Atr_5) ; \+ (rule_1___ifClause(Atr_1, Atr_2, Atr_3, Atr_4, _))
.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 

% Rule name: rule_2 
% If-complexes: 
rule_2___if_1(Atr_1, _, Atr_3, _, _) :- 
	Atr_1 > 6.0,
	Atr_3 > 5.0
.


% if-clause: 
rule_2___ifClause(Atr_1, _, Atr_3, _, _) :- 
	rule_2___if_1(Atr_1, _, Atr_3, _, _)
.


% Then-complexes: 
rule_2___then_1(_, _, _, _, Atr_5) :- 
	member(Atr_5, ['Iris-virginica'])
.


% then-clause: 
rule_2___thenClause(_, _, _, _, Atr_5) :- 
	rule_2___then_1(_, _, _, _, Atr_5)
.


% Whole rule: 
rule_2(Atr_1, _, Atr_3, _, Atr_5) :- 
	rule_2___thenClause(_, _, _, _, Atr_5) ; \+ (rule_2___ifClause(Atr_1, _, Atr_3, _, _))
.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 

% Rule name: rule_3 
% If-complexes: 
rule_3___if_1(Atr_1, Atr_2, Atr_3, Atr_4, _) :- 
	Atr_1 =< 6.2989999999999995,
	Atr_2 =< 3.1990000000000003,
	4.5 < Atr_3, Atr_3 =< 5.098999999999999,
	Atr_4 > 1.4
.


% if-clause: 
rule_3___ifClause(Atr_1, Atr_2, Atr_3, Atr_4, _) :- 
	rule_3___if_1(Atr_1, Atr_2, Atr_3, Atr_4, _)
.


% Then-complexes: 
rule_3___then_1(_, _, _, _, Atr_5) :- 
	member(Atr_5, ['Iris-virginica'])
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
rule_4___if_1(Atr_1, Atr_2, _, Atr_4, _) :- 
	Atr_1 =< 6.699,
	Atr_2 =< 3.099,
	Atr_4 > 1.6
.


% if-clause: 
rule_4___ifClause(Atr_1, Atr_2, _, Atr_4, _) :- 
	rule_4___if_1(Atr_1, Atr_2, _, Atr_4, _)
.


% Then-complexes: 
rule_4___then_1(_, _, _, _, Atr_5) :- 
	member(Atr_5, ['Iris-virginica'])
.


% then-clause: 
rule_4___thenClause(_, _, _, _, Atr_5) :- 
	rule_4___then_1(_, _, _, _, Atr_5)
.


% Whole rule: 
rule_4(Atr_1, Atr_2, _, Atr_4, Atr_5) :- 
	rule_4___thenClause(_, _, _, _, Atr_5) ; \+ (rule_4___ifClause(Atr_1, Atr_2, _, Atr_4, _))
.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 

% Rule name: rule_5 
% If-complexes: 
rule_5___if_1(Atr_1, Atr_2, Atr_3, Atr_4, _) :- 
	Atr_1 > 4.9,
	2.2 < Atr_2, Atr_2 =< 3.499,
	1.7 < Atr_3, Atr_3 =< 5.598999999999999,
	1.5 < Atr_4, Atr_4 =< 1.7990000000000002
.


% if-clause: 
rule_5___ifClause(Atr_1, Atr_2, Atr_3, Atr_4, _) :- 
	rule_5___if_1(Atr_1, Atr_2, Atr_3, Atr_4, _)
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
rule_5(Atr_1, Atr_2, Atr_3, Atr_4, Atr_5) :- 
	rule_5___thenClause(_, _, _, _, Atr_5) ; \+ (rule_5___ifClause(Atr_1, Atr_2, Atr_3, Atr_4, _))
.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 

% Rule name: rule_6 
% If-complexes: 
rule_6___if_1(Atr_1, Atr_2, Atr_3, _, _) :- 
	4.8 < Atr_1, Atr_1 =< 6.699,
	2.2 < Atr_2, Atr_2 =< 2.499,
	Atr_3 > 1.2
.


% if-clause: 
rule_6___ifClause(Atr_1, Atr_2, Atr_3, _, _) :- 
	rule_6___if_1(Atr_1, Atr_2, Atr_3, _, _)
.


% Then-complexes: 
rule_6___then_1(_, _, _, _, Atr_5) :- 
	member(Atr_5, ['Iris-versicolor'])
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
	5.3 < Atr_1, Atr_1 =< 6.899,
	3.0 < Atr_2, Atr_2 =< 3.999,
	Atr_3 =< 5.098999999999999,
	Atr_4 > 0.4
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


