%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 

% Rule name: rule_1 
% If-complexes: 
rule_1___if_1(Atr_1, _, Atr_3, _, Atr_5, _) :- 
	45.0 < Atr_1, Atr_1 =< 111.999,
	1.0 < Atr_3, Atr_3 =< 3.999,
	1.0 < Atr_5, Atr_5 =< 3.999
.


% if-clause: 
rule_1___ifClause(Atr_1, _, Atr_3, _, Atr_5, _) :- 
	rule_1___if_1(Atr_1, _, Atr_3, _, Atr_5, _)
.


% Then-complexes: 
rule_1___then_1(_, _, _, _, _, Atr_6) :- 
	member(Atr_6, ['s'])
.


% then-clause: 
rule_1___thenClause(_, _, _, _, _, Atr_6) :- 
	rule_1___then_1(_, _, _, _, _, Atr_6)
.


% Whole rule: 
rule_1(Atr_1, _, Atr_3, _, Atr_5, Atr_6) :- 
	rule_1___thenClause(_, _, _, _, _, Atr_6) ; \+ (rule_1___ifClause(Atr_1, _, Atr_3, _, Atr_5, _))
.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 

% Rule name: rule_2 
% If-complexes: 
rule_2___if_1(Atr_1, Atr_2, Atr_3, _, Atr_5, _) :- 
	Atr_1 =< 20.999,
	Atr_2 =< 1.999,
	2.0 < Atr_3, Atr_3 =< 3.999,
	Atr_5 =< 1.999
.


% if-clause: 
rule_2___ifClause(Atr_1, Atr_2, Atr_3, _, Atr_5, _) :- 
	rule_2___if_1(Atr_1, Atr_2, Atr_3, _, Atr_5, _)
.


% Then-complexes: 
rule_2___then_1(_, _, _, _, _, Atr_6) :- 
	member(Atr_6, ['f'])
.


% then-clause: 
rule_2___thenClause(_, _, _, _, _, Atr_6) :- 
	rule_2___then_1(_, _, _, _, _, Atr_6)
.


% Whole rule: 
rule_2(Atr_1, Atr_2, Atr_3, _, Atr_5, Atr_6) :- 
	rule_2___thenClause(_, _, _, _, _, Atr_6) ; \+ (rule_2___ifClause(Atr_1, Atr_2, Atr_3, _, Atr_5, _))
.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 

% Rule name: rule_3 
% If-complexes: 
rule_3___if_1(_, _, _, Atr_4, _, _) :- 
	Atr_4 > 3.0
.


% if-clause: 
rule_3___ifClause(_, _, _, Atr_4, _, _) :- 
	rule_3___if_1(_, _, _, Atr_4, _, _)
.


% Then-complexes: 
rule_3___then_1(_, _, _, _, _, Atr_6) :- 
	member(Atr_6, ['t'])
.


% then-clause: 
rule_3___thenClause(_, _, _, _, _, Atr_6) :- 
	rule_3___then_1(_, _, _, _, _, Atr_6)
.


% Whole rule: 
rule_3(_, _, _, Atr_4, _, Atr_6) :- 
	rule_3___thenClause(_, _, _, _, _, Atr_6) ; \+ (rule_3___ifClause(_, _, _, Atr_4, _, _))
.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 

% Rule name: rule_4 
% If-complexes: 
rule_4___if_1(Atr_1, _, Atr_3, Atr_4, Atr_5, _) :- 
	Atr_1 > 84.0,
	Atr_3 =< 2.999,
	Atr_4 =< 3.999,
	Atr_5 =< 1.999
.


% if-clause: 
rule_4___ifClause(Atr_1, _, Atr_3, Atr_4, Atr_5, _) :- 
	rule_4___if_1(Atr_1, _, Atr_3, Atr_4, Atr_5, _)
.


% Then-complexes: 
rule_4___then_1(_, _, _, _, _, Atr_6) :- 
	member(Atr_6, ['f'])
.


% then-clause: 
rule_4___thenClause(_, _, _, _, _, Atr_6) :- 
	rule_4___then_1(_, _, _, _, _, Atr_6)
.


% Whole rule: 
rule_4(Atr_1, _, Atr_3, Atr_4, Atr_5, Atr_6) :- 
	rule_4___thenClause(_, _, _, _, _, Atr_6) ; \+ (rule_4___ifClause(Atr_1, _, Atr_3, Atr_4, Atr_5, _))
.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 

% Rule name: rule_5 
% If-complexes: 
rule_5___if_1(Atr_1, Atr_2, Atr_3, Atr_4, Atr_5, _) :- 
	Atr_1 =< 88.999,
	Atr_2 > 1.0,
	Atr_3 =< 3.999,
	1.0 < Atr_4, Atr_4 =< 3.999,
	1.0 < Atr_5, Atr_5 =< 3.999
.


% if-clause: 
rule_5___ifClause(Atr_1, Atr_2, Atr_3, Atr_4, Atr_5, _) :- 
	rule_5___if_1(Atr_1, Atr_2, Atr_3, Atr_4, Atr_5, _)
.


% Then-complexes: 
rule_5___then_1(_, _, _, _, _, Atr_6) :- 
	member(Atr_6, ['s'])
.


% then-clause: 
rule_5___thenClause(_, _, _, _, _, Atr_6) :- 
	rule_5___then_1(_, _, _, _, _, Atr_6)
.


% Whole rule: 
rule_5(Atr_1, Atr_2, Atr_3, Atr_4, Atr_5, Atr_6) :- 
	rule_5___thenClause(_, _, _, _, _, Atr_6) ; \+ (rule_5___ifClause(Atr_1, Atr_2, Atr_3, Atr_4, Atr_5, _))
.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 

% Rule name: rule_6 
% If-complexes: 
rule_6___if_1(Atr_1, _, Atr_3, Atr_4, Atr_5, _) :- 
	Atr_1 =< 92.999,
	1.0 < Atr_3, Atr_3 =< 2.999,
	1.0 < Atr_4, Atr_4 =< 3.999,
	Atr_5 =< 3.999
.


% if-clause: 
rule_6___ifClause(Atr_1, _, Atr_3, Atr_4, Atr_5, _) :- 
	rule_6___if_1(Atr_1, _, Atr_3, Atr_4, Atr_5, _)
.


% Then-complexes: 
rule_6___then_1(_, _, _, _, _, Atr_6) :- 
	member(Atr_6, ['s'])
.


% then-clause: 
rule_6___thenClause(_, _, _, _, _, Atr_6) :- 
	rule_6___then_1(_, _, _, _, _, Atr_6)
.


% Whole rule: 
rule_6(Atr_1, _, Atr_3, Atr_4, Atr_5, Atr_6) :- 
	rule_6___thenClause(_, _, _, _, _, Atr_6) ; \+ (rule_6___ifClause(Atr_1, _, Atr_3, Atr_4, Atr_5, _))
.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 

% Rule name: rule_7 
% If-complexes: 
rule_7___if_1(_, _, Atr_3, _, _, _) :- 
	Atr_3 > 3.0
.


% if-clause: 
rule_7___ifClause(_, _, Atr_3, _, _, _) :- 
	rule_7___if_1(_, _, Atr_3, _, _, _)
.


% Then-complexes: 
rule_7___then_1(_, _, _, _, _, Atr_6) :- 
	member(Atr_6, ['t'])
.


% then-clause: 
rule_7___thenClause(_, _, _, _, _, Atr_6) :- 
	rule_7___then_1(_, _, _, _, _, Atr_6)
.


% Whole rule: 
rule_7(_, _, Atr_3, _, _, Atr_6) :- 
	rule_7___thenClause(_, _, _, _, _, Atr_6) ; \+ (rule_7___ifClause(_, _, Atr_3, _, _, _))
.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 

% Rule name: rule_8 
% If-complexes: 
rule_8___if_1(Atr_1, _, Atr_3, Atr_4, Atr_5, _) :- 
	Atr_1 =< 114.999,
	Atr_3 =< 1.999,
	Atr_4 =< 1.999,
	Atr_5 =< 3.999
.


% if-clause: 
rule_8___ifClause(Atr_1, _, Atr_3, Atr_4, Atr_5, _) :- 
	rule_8___if_1(Atr_1, _, Atr_3, Atr_4, Atr_5, _)
.


% Then-complexes: 
rule_8___then_1(_, _, _, _, _, Atr_6) :- 
	member(Atr_6, ['f'])
.


% then-clause: 
rule_8___thenClause(_, _, _, _, _, Atr_6) :- 
	rule_8___then_1(_, _, _, _, _, Atr_6)
.


% Whole rule: 
rule_8(Atr_1, _, Atr_3, Atr_4, Atr_5, Atr_6) :- 
	rule_8___thenClause(_, _, _, _, _, Atr_6) ; \+ (rule_8___ifClause(Atr_1, _, Atr_3, Atr_4, Atr_5, _))
.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 

% Rule name: rule_9 
% If-complexes: 
rule_9___if_1(_, _, Atr_3, Atr_4, Atr_5, _) :- 
	Atr_3 =< 3.999,
	Atr_4 =< 1.999,
	Atr_5 =< 1.999
.


% if-clause: 
rule_9___ifClause(_, _, Atr_3, Atr_4, Atr_5, _) :- 
	rule_9___if_1(_, _, Atr_3, Atr_4, Atr_5, _)
.


% Then-complexes: 
rule_9___then_1(_, _, _, _, _, Atr_6) :- 
	member(Atr_6, ['f'])
.


% then-clause: 
rule_9___thenClause(_, _, _, _, _, Atr_6) :- 
	rule_9___then_1(_, _, _, _, _, Atr_6)
.


% Whole rule: 
rule_9(_, _, Atr_3, Atr_4, Atr_5, Atr_6) :- 
	rule_9___thenClause(_, _, _, _, _, Atr_6) ; \+ (rule_9___ifClause(_, _, Atr_3, Atr_4, Atr_5, _))
.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 

% Rule name: rule_10 
% If-complexes: 
rule_10___if_1(_, _, _, _, Atr_5, _) :- 
	Atr_5 > 3.0
.


% if-clause: 
rule_10___ifClause(_, _, _, _, Atr_5, _) :- 
	rule_10___if_1(_, _, _, _, Atr_5, _)
.


% Then-complexes: 
rule_10___then_1(_, _, _, _, _, Atr_6) :- 
	member(Atr_6, ['t'])
.


% then-clause: 
rule_10___thenClause(_, _, _, _, _, Atr_6) :- 
	rule_10___then_1(_, _, _, _, _, Atr_6)
.


% Whole rule: 
rule_10(_, _, _, _, Atr_5, Atr_6) :- 
	rule_10___thenClause(_, _, _, _, _, Atr_6) ; \+ (rule_10___ifClause(_, _, _, _, Atr_5, _))
.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 

% Rule name: rule_11 
% If-complexes: 
rule_11___if_1(Atr_1, _, Atr_3, Atr_4, Atr_5, _) :- 
	9.0 < Atr_1, Atr_1 =< 129.999,
	1.0 < Atr_3, Atr_3 =< 3.999,
	Atr_4 =< 3.999,
	1.0 < Atr_5, Atr_5 =< 2.999
.


% if-clause: 
rule_11___ifClause(Atr_1, _, Atr_3, Atr_4, Atr_5, _) :- 
	rule_11___if_1(Atr_1, _, Atr_3, Atr_4, Atr_5, _)
.


% Then-complexes: 
rule_11___then_1(_, _, _, _, _, Atr_6) :- 
	member(Atr_6, ['s'])
.


% then-clause: 
rule_11___thenClause(_, _, _, _, _, Atr_6) :- 
	rule_11___then_1(_, _, _, _, _, Atr_6)
.


% Whole rule: 
rule_11(Atr_1, _, Atr_3, Atr_4, Atr_5, Atr_6) :- 
	rule_11___thenClause(_, _, _, _, _, Atr_6) ; \+ (rule_11___ifClause(Atr_1, _, Atr_3, Atr_4, Atr_5, _))
.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 

% Rule name: rule_12 
% If-complexes: 
rule_12___if_1(Atr_1, _, Atr_3, Atr_4, Atr_5, _) :- 
	55.0 < Atr_1, Atr_1 =< 109.999,
	Atr_3 > 2.0,
	Atr_4 =< 3.999,
	Atr_5 =< 1.999
.


% if-clause: 
rule_12___ifClause(Atr_1, _, Atr_3, Atr_4, Atr_5, _) :- 
	rule_12___if_1(Atr_1, _, Atr_3, Atr_4, Atr_5, _)
.


% Then-complexes: 
rule_12___then_1(_, _, _, _, _, Atr_6) :- 
	member(Atr_6, ['f'])
.


% then-clause: 
rule_12___thenClause(_, _, _, _, _, Atr_6) :- 
	rule_12___then_1(_, _, _, _, _, Atr_6)
.


% Whole rule: 
rule_12(Atr_1, _, Atr_3, Atr_4, Atr_5, Atr_6) :- 
	rule_12___thenClause(_, _, _, _, _, Atr_6) ; \+ (rule_12___ifClause(Atr_1, _, Atr_3, Atr_4, Atr_5, _))
.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 

% Rule name: rule_13 
% If-complexes: 
rule_13___if_1(Atr_1, Atr_2, Atr_3, _, _, _) :- 
	Atr_1 > 116.0,
	Atr_2 > 1.0,
	Atr_3 =< 1.999
.


% if-clause: 
rule_13___ifClause(Atr_1, Atr_2, Atr_3, _, _, _) :- 
	rule_13___if_1(Atr_1, Atr_2, Atr_3, _, _, _)
.


% Then-complexes: 
rule_13___then_1(_, _, _, _, _, Atr_6) :- 
	member(Atr_6, ['f'])
.


% then-clause: 
rule_13___thenClause(_, _, _, _, _, Atr_6) :- 
	rule_13___then_1(_, _, _, _, _, Atr_6)
.


% Whole rule: 
rule_13(Atr_1, Atr_2, Atr_3, _, _, Atr_6) :- 
	rule_13___thenClause(_, _, _, _, _, Atr_6) ; \+ (rule_13___ifClause(Atr_1, Atr_2, Atr_3, _, _, _))
.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 

% Rule name: rule_14 
% If-complexes: 
rule_14___if_1(Atr_1, Atr_2, Atr_3, Atr_4, Atr_5, _) :- 
	Atr_1 > 109.0,
	Atr_2 > 1.0,
	Atr_3 > 1.0,
	Atr_4 =< 1.999,
	Atr_5 > 2.0
.


% if-clause: 
rule_14___ifClause(Atr_1, Atr_2, Atr_3, Atr_4, Atr_5, _) :- 
	rule_14___if_1(Atr_1, Atr_2, Atr_3, Atr_4, Atr_5, _)
.


% Then-complexes: 
rule_14___then_1(_, _, _, _, _, Atr_6) :- 
	member(Atr_6, ['f'])
.


% then-clause: 
rule_14___thenClause(_, _, _, _, _, Atr_6) :- 
	rule_14___then_1(_, _, _, _, _, Atr_6)
.


% Whole rule: 
rule_14(Atr_1, Atr_2, Atr_3, Atr_4, Atr_5, Atr_6) :- 
	rule_14___thenClause(_, _, _, _, _, Atr_6) ; \+ (rule_14___ifClause(Atr_1, Atr_2, Atr_3, Atr_4, Atr_5, _))
.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 

% Rule name: rule_15 
% If-complexes: 
rule_15___if_1(Atr_1, _, Atr_3, Atr_4, Atr_5, _) :- 
	3.0 < Atr_1, Atr_1 =< 6.999,
	1.0 < Atr_3, Atr_3 =< 2.999,
	Atr_4 =< 3.999,
	Atr_5 =< 3.999
.


% if-clause: 
rule_15___ifClause(Atr_1, _, Atr_3, Atr_4, Atr_5, _) :- 
	rule_15___if_1(Atr_1, _, Atr_3, Atr_4, Atr_5, _)
.


% Then-complexes: 
rule_15___then_1(_, _, _, _, _, Atr_6) :- 
	member(Atr_6, ['s'])
.


% then-clause: 
rule_15___thenClause(_, _, _, _, _, Atr_6) :- 
	rule_15___then_1(_, _, _, _, _, Atr_6)
.


% Whole rule: 
rule_15(Atr_1, _, Atr_3, Atr_4, Atr_5, Atr_6) :- 
	rule_15___thenClause(_, _, _, _, _, Atr_6) ; \+ (rule_15___ifClause(Atr_1, _, Atr_3, Atr_4, Atr_5, _))
.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 

% Rule name: rule_16 
% If-complexes: 
rule_16___if_1(Atr_1, Atr_2, Atr_3, Atr_4, Atr_5, _) :- 
	fail,
	Atr_2 > 1.0,
	Atr_3 =< 1.999,
	2.0 < Atr_4, Atr_4 =< 3.999,
	Atr_5 =< 3.999
.


% if-clause: 
rule_16___ifClause(Atr_1, Atr_2, Atr_3, Atr_4, Atr_5, _) :- 
	rule_16___if_1(Atr_1, Atr_2, Atr_3, Atr_4, Atr_5, _)
.


% Then-complexes: 
rule_16___then_1(_, _, _, _, _, Atr_6) :- 
	member(Atr_6, ['f'])
.


% then-clause: 
rule_16___thenClause(_, _, _, _, _, Atr_6) :- 
	rule_16___then_1(_, _, _, _, _, Atr_6)
.


% Whole rule: 
rule_16(Atr_1, Atr_2, Atr_3, Atr_4, Atr_5, Atr_6) :- 
	rule_16___thenClause(_, _, _, _, _, Atr_6) ; \+ (rule_16___ifClause(Atr_1, Atr_2, Atr_3, Atr_4, Atr_5, _))
.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 

% Rule name: rule_17 
% If-complexes: 
rule_17___if_1(Atr_1, _, Atr_3, Atr_4, Atr_5, _) :- 
	Atr_1 =< 125.999,
	Atr_3 =< 3.999,
	1.0 < Atr_4, Atr_4 =< 2.999,
	1.0 < Atr_5, Atr_5 =< 3.999
.


% if-clause: 
rule_17___ifClause(Atr_1, _, Atr_3, Atr_4, Atr_5, _) :- 
	rule_17___if_1(Atr_1, _, Atr_3, Atr_4, Atr_5, _)
.


% Then-complexes: 
rule_17___then_1(_, _, _, _, _, Atr_6) :- 
	member(Atr_6, ['s'])
.


% then-clause: 
rule_17___thenClause(_, _, _, _, _, Atr_6) :- 
	rule_17___then_1(_, _, _, _, _, Atr_6)
.


% Whole rule: 
rule_17(Atr_1, _, Atr_3, Atr_4, Atr_5, Atr_6) :- 
	rule_17___thenClause(_, _, _, _, _, Atr_6) ; \+ (rule_17___ifClause(Atr_1, _, Atr_3, Atr_4, Atr_5, _))
.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 

% Rule name: rule_18 
% If-complexes: 
rule_18___if_1(Atr_1, _, Atr_3, Atr_4, _, _) :- 
	fail,
	Atr_3 =< 1.999,
	Atr_4 > 1.0
.


% if-clause: 
rule_18___ifClause(Atr_1, _, Atr_3, Atr_4, _, _) :- 
	rule_18___if_1(Atr_1, _, Atr_3, Atr_4, _, _)
.


% Then-complexes: 
rule_18___then_1(_, _, _, _, _, Atr_6) :- 
	member(Atr_6, ['f'])
.


% then-clause: 
rule_18___thenClause(_, _, _, _, _, Atr_6) :- 
	rule_18___then_1(_, _, _, _, _, Atr_6)
.


% Whole rule: 
rule_18(Atr_1, _, Atr_3, Atr_4, _, Atr_6) :- 
	rule_18___thenClause(_, _, _, _, _, Atr_6) ; \+ (rule_18___ifClause(Atr_1, _, Atr_3, Atr_4, _, _))
.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 

% Rule name: rule_19 
% If-complexes: 
rule_19___if_1(Atr_1, _, Atr_3, Atr_4, Atr_5, _) :- 
	10.0 < Atr_1, Atr_1 =< 22.999,
	Atr_3 =< 3.999,
	Atr_4 =< 3.999,
	Atr_5 > 1.0
.


% if-clause: 
rule_19___ifClause(Atr_1, _, Atr_3, Atr_4, Atr_5, _) :- 
	rule_19___if_1(Atr_1, _, Atr_3, Atr_4, Atr_5, _)
.


% Then-complexes: 
rule_19___then_1(_, _, _, _, _, Atr_6) :- 
	member(Atr_6, ['f'])
.


% then-clause: 
rule_19___thenClause(_, _, _, _, _, Atr_6) :- 
	rule_19___then_1(_, _, _, _, _, Atr_6)
.


% Whole rule: 
rule_19(Atr_1, _, Atr_3, Atr_4, Atr_5, Atr_6) :- 
	rule_19___thenClause(_, _, _, _, _, Atr_6) ; \+ (rule_19___ifClause(Atr_1, _, Atr_3, Atr_4, Atr_5, _))
.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 

% Rule name: rule_20 
% If-complexes: 
rule_20___if_1(Atr_1, Atr_2, Atr_3, Atr_4, _, _) :- 
	fail,
	Atr_2 > 2.0,
	1.0 < Atr_3, Atr_3 =< 3.999,
	Atr_4 =< 3.999
.


% if-clause: 
rule_20___ifClause(Atr_1, Atr_2, Atr_3, Atr_4, _, _) :- 
	rule_20___if_1(Atr_1, Atr_2, Atr_3, Atr_4, _, _)
.


% Then-complexes: 
rule_20___then_1(_, _, _, _, _, Atr_6) :- 
	member(Atr_6, ['s'])
.


% then-clause: 
rule_20___thenClause(_, _, _, _, _, Atr_6) :- 
	rule_20___then_1(_, _, _, _, _, Atr_6)
.


% Whole rule: 
rule_20(Atr_1, Atr_2, Atr_3, Atr_4, _, Atr_6) :- 
	rule_20___thenClause(_, _, _, _, _, Atr_6) ; \+ (rule_20___ifClause(Atr_1, Atr_2, Atr_3, Atr_4, _, _))
.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 

% Rule name: rule_21 
% If-complexes: 
rule_21___if_1(Atr_1, Atr_2, Atr_3, Atr_4, _, _) :- 
	13.0 < Atr_1, Atr_1 =< 67.999,
	Atr_2 > 1.0,
	2.0 < Atr_3, Atr_3 =< 3.999,
	Atr_4 > 1.0
.


% if-clause: 
rule_21___ifClause(Atr_1, Atr_2, Atr_3, Atr_4, _, _) :- 
	rule_21___if_1(Atr_1, Atr_2, Atr_3, Atr_4, _, _)
.


% Then-complexes: 
rule_21___then_1(_, _, _, _, _, Atr_6) :- 
	member(Atr_6, ['s'])
.


% then-clause: 
rule_21___thenClause(_, _, _, _, _, Atr_6) :- 
	rule_21___then_1(_, _, _, _, _, Atr_6)
.


% Whole rule: 
rule_21(Atr_1, Atr_2, Atr_3, Atr_4, _, Atr_6) :- 
	rule_21___thenClause(_, _, _, _, _, Atr_6) ; \+ (rule_21___ifClause(Atr_1, Atr_2, Atr_3, Atr_4, _, _))
.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 

% Rule name: rule_22 
% If-complexes: 
rule_22___if_1(Atr_1, Atr_2, Atr_3, Atr_4, Atr_5, _) :- 
	35.0 < Atr_1, Atr_1 =< 95.999,
	Atr_2 =< 1.999,
	Atr_3 =< 2.999,
	1.0 < Atr_4, Atr_4 =< 3.999,
	Atr_5 > 1.0
.


% if-clause: 
rule_22___ifClause(Atr_1, Atr_2, Atr_3, Atr_4, Atr_5, _) :- 
	rule_22___if_1(Atr_1, Atr_2, Atr_3, Atr_4, Atr_5, _)
.


% Then-complexes: 
rule_22___then_1(_, _, _, _, _, Atr_6) :- 
	member(Atr_6, ['s'])
.


% then-clause: 
rule_22___thenClause(_, _, _, _, _, Atr_6) :- 
	rule_22___then_1(_, _, _, _, _, Atr_6)
.


% Whole rule: 
rule_22(Atr_1, Atr_2, Atr_3, Atr_4, Atr_5, Atr_6) :- 
	rule_22___thenClause(_, _, _, _, _, Atr_6) ; \+ (rule_22___ifClause(Atr_1, Atr_2, Atr_3, Atr_4, Atr_5, _))
.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 

% Rule name: rule_23 
% If-complexes: 
rule_23___if_1(Atr_1, Atr_2, Atr_3, _, Atr_5, _) :- 
	34.0 < Atr_1, Atr_1 =< 62.999,
	1.0 < Atr_2, Atr_2 =< 2.999,
	Atr_3 > 1.0,
	Atr_5 > 2.0
.


% if-clause: 
rule_23___ifClause(Atr_1, Atr_2, Atr_3, _, Atr_5, _) :- 
	rule_23___if_1(Atr_1, Atr_2, Atr_3, _, Atr_5, _)
.


% Then-complexes: 
rule_23___then_1(_, _, _, _, _, Atr_6) :- 
	member(Atr_6, ['f'])
.


% then-clause: 
rule_23___thenClause(_, _, _, _, _, Atr_6) :- 
	rule_23___then_1(_, _, _, _, _, Atr_6)
.


% Whole rule: 
rule_23(Atr_1, Atr_2, Atr_3, _, Atr_5, Atr_6) :- 
	rule_23___thenClause(_, _, _, _, _, Atr_6) ; \+ (rule_23___ifClause(Atr_1, Atr_2, Atr_3, _, Atr_5, _))
.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 

% Rule name: rule_24 
% If-complexes: 
rule_24___if_1(Atr_1, Atr_2, Atr_3, Atr_4, Atr_5, _) :- 
	Atr_1 > 35.0,
	Atr_2 =< 1.999,
	1.0 < Atr_3, Atr_3 =< 2.999,
	Atr_4 =< 3.999,
	Atr_5 > 1.0
.


% if-clause: 
rule_24___ifClause(Atr_1, Atr_2, Atr_3, Atr_4, Atr_5, _) :- 
	rule_24___if_1(Atr_1, Atr_2, Atr_3, Atr_4, Atr_5, _)
.


% Then-complexes: 
rule_24___then_1(_, _, _, _, _, Atr_6) :- 
	member(Atr_6, ['s'])
.


% then-clause: 
rule_24___thenClause(_, _, _, _, _, Atr_6) :- 
	rule_24___then_1(_, _, _, _, _, Atr_6)
.


% Whole rule: 
rule_24(Atr_1, Atr_2, Atr_3, Atr_4, Atr_5, Atr_6) :- 
	rule_24___thenClause(_, _, _, _, _, Atr_6) ; \+ (rule_24___ifClause(Atr_1, Atr_2, Atr_3, Atr_4, Atr_5, _))
.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 

% Rule name: rule_25 
% If-complexes: 
rule_25___if_1(Atr_1, Atr_2, Atr_3, Atr_4, _, _) :- 
	Atr_1 =< 36.999,
	Atr_2 =< 1.999,
	Atr_3 =< 2.999,
	Atr_4 =< 1.999
.


% if-clause: 
rule_25___ifClause(Atr_1, Atr_2, Atr_3, Atr_4, _, _) :- 
	rule_25___if_1(Atr_1, Atr_2, Atr_3, Atr_4, _, _)
.


% Then-complexes: 
rule_25___then_1(_, _, _, _, _, Atr_6) :- 
	member(Atr_6, ['f'])
.


% then-clause: 
rule_25___thenClause(_, _, _, _, _, Atr_6) :- 
	rule_25___then_1(_, _, _, _, _, Atr_6)
.


% Whole rule: 
rule_25(Atr_1, Atr_2, Atr_3, Atr_4, _, Atr_6) :- 
	rule_25___thenClause(_, _, _, _, _, Atr_6) ; \+ (rule_25___ifClause(Atr_1, Atr_2, Atr_3, Atr_4, _, _))
.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 

% Rule name: rule_26 
% If-complexes: 
rule_26___if_1(Atr_1, Atr_2, Atr_3, Atr_4, _, _) :- 
	fail,
	Atr_2 =< 2.999,
	Atr_3 =< 1.999,
	2.0 < Atr_4, Atr_4 =< 3.999
.


% if-clause: 
rule_26___ifClause(Atr_1, Atr_2, Atr_3, Atr_4, _, _) :- 
	rule_26___if_1(Atr_1, Atr_2, Atr_3, Atr_4, _, _)
.


% Then-complexes: 
rule_26___then_1(_, _, _, _, _, Atr_6) :- 
	member(Atr_6, ['f'])
.


% then-clause: 
rule_26___thenClause(_, _, _, _, _, Atr_6) :- 
	rule_26___then_1(_, _, _, _, _, Atr_6)
.


% Whole rule: 
rule_26(Atr_1, Atr_2, Atr_3, Atr_4, _, Atr_6) :- 
	rule_26___thenClause(_, _, _, _, _, Atr_6) ; \+ (rule_26___ifClause(Atr_1, Atr_2, Atr_3, Atr_4, _, _))
.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 

% Rule name: rule_27 
% If-complexes: 
rule_27___if_1(Atr_1, _, Atr_3, Atr_4, _, _) :- 
	Atr_1 > 113.0,
	Atr_3 =< 1.999,
	2.0 < Atr_4, Atr_4 =< 3.999
.


% if-clause: 
rule_27___ifClause(Atr_1, _, Atr_3, Atr_4, _, _) :- 
	rule_27___if_1(Atr_1, _, Atr_3, Atr_4, _, _)
.


% Then-complexes: 
rule_27___then_1(_, _, _, _, _, Atr_6) :- 
	member(Atr_6, ['f'])
.


% then-clause: 
rule_27___thenClause(_, _, _, _, _, Atr_6) :- 
	rule_27___then_1(_, _, _, _, _, Atr_6)
.


% Whole rule: 
rule_27(Atr_1, _, Atr_3, Atr_4, _, Atr_6) :- 
	rule_27___thenClause(_, _, _, _, _, Atr_6) ; \+ (rule_27___ifClause(Atr_1, _, Atr_3, Atr_4, _, _))
.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 

% Rule name: rule_28 
% If-complexes: 
rule_28___if_1(Atr_1, Atr_2, Atr_3, Atr_4, _, _) :- 
	80.0 < Atr_1, Atr_1 =< 115.999,
	Atr_2 > 2.0,
	Atr_3 =< 2.999,
	1.0 < Atr_4, Atr_4 =< 3.999
.


% if-clause: 
rule_28___ifClause(Atr_1, Atr_2, Atr_3, Atr_4, _, _) :- 
	rule_28___if_1(Atr_1, Atr_2, Atr_3, Atr_4, _, _)
.


% Then-complexes: 
rule_28___then_1(_, _, _, _, _, Atr_6) :- 
	member(Atr_6, ['f'])
.


% then-clause: 
rule_28___thenClause(_, _, _, _, _, Atr_6) :- 
	rule_28___then_1(_, _, _, _, _, Atr_6)
.


% Whole rule: 
rule_28(Atr_1, Atr_2, Atr_3, Atr_4, _, Atr_6) :- 
	rule_28___thenClause(_, _, _, _, _, Atr_6) ; \+ (rule_28___ifClause(Atr_1, Atr_2, Atr_3, Atr_4, _, _))
.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 

% Rule name: rule_29 
% If-complexes: 
rule_29___if_1(Atr_1, Atr_2, Atr_3, _, _, _) :- 
	110.0 < Atr_1, Atr_1 =< 130.999,
	Atr_2 =< 2.999,
	Atr_3 > 2.0
.


% if-clause: 
rule_29___ifClause(Atr_1, Atr_2, Atr_3, _, _, _) :- 
	rule_29___if_1(Atr_1, Atr_2, Atr_3, _, _, _)
.


% Then-complexes: 
rule_29___then_1(_, _, _, _, _, Atr_6) :- 
	member(Atr_6, ['s'])
.


% then-clause: 
rule_29___thenClause(_, _, _, _, _, Atr_6) :- 
	rule_29___then_1(_, _, _, _, _, Atr_6)
.


% Whole rule: 
rule_29(Atr_1, Atr_2, Atr_3, _, _, Atr_6) :- 
	rule_29___thenClause(_, _, _, _, _, Atr_6) ; \+ (rule_29___ifClause(Atr_1, Atr_2, Atr_3, _, _, _))
.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 

% Rule name: rule_30 
% If-complexes: 
rule_30___if_1(Atr_1, Atr_2, Atr_3, Atr_4, Atr_5, _) :- 
	fail,
	Atr_2 =< 1.999,
	Atr_3 =< 1.999,
	Atr_4 > 1.0,
	Atr_5 =< 2.999
.


% if-clause: 
rule_30___ifClause(Atr_1, Atr_2, Atr_3, Atr_4, Atr_5, _) :- 
	rule_30___if_1(Atr_1, Atr_2, Atr_3, Atr_4, Atr_5, _)
.


% Then-complexes: 
rule_30___then_1(_, _, _, _, _, Atr_6) :- 
	member(Atr_6, ['f'])
.


% then-clause: 
rule_30___thenClause(_, _, _, _, _, Atr_6) :- 
	rule_30___then_1(_, _, _, _, _, Atr_6)
.


% Whole rule: 
rule_30(Atr_1, Atr_2, Atr_3, Atr_4, Atr_5, Atr_6) :- 
	rule_30___thenClause(_, _, _, _, _, Atr_6) ; \+ (rule_30___ifClause(Atr_1, Atr_2, Atr_3, Atr_4, Atr_5, _))
.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 

% Rule name: rule_31 
% If-complexes: 
rule_31___if_1(Atr_1, Atr_2, Atr_3, _, Atr_5, _) :- 
	19.0 < Atr_1, Atr_1 =< 118.999,
	Atr_2 > 2.0,
	1.0 < Atr_3, Atr_3 =< 2.999,
	1.0 < Atr_5, Atr_5 =< 3.999
.


% if-clause: 
rule_31___ifClause(Atr_1, Atr_2, Atr_3, _, Atr_5, _) :- 
	rule_31___if_1(Atr_1, Atr_2, Atr_3, _, Atr_5, _)
.


% Then-complexes: 
rule_31___then_1(_, _, _, _, _, Atr_6) :- 
	member(Atr_6, ['s'])
.


% then-clause: 
rule_31___thenClause(_, _, _, _, _, Atr_6) :- 
	rule_31___then_1(_, _, _, _, _, Atr_6)
.


% Whole rule: 
rule_31(Atr_1, Atr_2, Atr_3, _, Atr_5, Atr_6) :- 
	rule_31___thenClause(_, _, _, _, _, Atr_6) ; \+ (rule_31___ifClause(Atr_1, Atr_2, Atr_3, _, Atr_5, _))
.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 

% Rule name: rule_32 
% If-complexes: 
rule_32___if_1(Atr_1, Atr_2, Atr_3, Atr_4, _, _) :- 
	fail,
	Atr_2 =< 2.999,
	Atr_3 =< 1.999,
	Atr_4 > 2.0
.


% if-clause: 
rule_32___ifClause(Atr_1, Atr_2, Atr_3, Atr_4, _, _) :- 
	rule_32___if_1(Atr_1, Atr_2, Atr_3, Atr_4, _, _)
.


% Then-complexes: 
rule_32___then_1(_, _, _, _, _, Atr_6) :- 
	member(Atr_6, ['f'])
.


% then-clause: 
rule_32___thenClause(_, _, _, _, _, Atr_6) :- 
	rule_32___then_1(_, _, _, _, _, Atr_6)
.


% Whole rule: 
rule_32(Atr_1, Atr_2, Atr_3, Atr_4, _, Atr_6) :- 
	rule_32___thenClause(_, _, _, _, _, Atr_6) ; \+ (rule_32___ifClause(Atr_1, Atr_2, Atr_3, Atr_4, _, _))
.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 

% Rule name: rule_33 
% If-complexes: 
rule_33___if_1(Atr_1, Atr_2, _, _, _, _) :- 
	fail,
	Atr_2 =< 1.999
.


% if-clause: 
rule_33___ifClause(Atr_1, Atr_2, _, _, _, _) :- 
	rule_33___if_1(Atr_1, Atr_2, _, _, _, _)
.


% Then-complexes: 
rule_33___then_1(_, _, _, _, _, Atr_6) :- 
	member(Atr_6, ['s'])
.


% then-clause: 
rule_33___thenClause(_, _, _, _, _, Atr_6) :- 
	rule_33___then_1(_, _, _, _, _, Atr_6)
.


% Whole rule: 
rule_33(Atr_1, Atr_2, _, _, _, Atr_6) :- 
	rule_33___thenClause(_, _, _, _, _, Atr_6) ; \+ (rule_33___ifClause(Atr_1, Atr_2, _, _, _, _))
.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 

% Rule name: rule_34 
% If-complexes: 
rule_34___if_1(Atr_1, Atr_2, Atr_3, _, Atr_5, _) :- 
	10.0 < Atr_1, Atr_1 =< 69.999,
	Atr_2 =< 2.999,
	Atr_3 =< 1.999,
	Atr_5 =< 1.999
.


% if-clause: 
rule_34___ifClause(Atr_1, Atr_2, Atr_3, _, Atr_5, _) :- 
	rule_34___if_1(Atr_1, Atr_2, Atr_3, _, Atr_5, _)
.


% Then-complexes: 
rule_34___then_1(_, _, _, _, _, Atr_6) :- 
	member(Atr_6, ['f'])
.


% then-clause: 
rule_34___thenClause(_, _, _, _, _, Atr_6) :- 
	rule_34___then_1(_, _, _, _, _, Atr_6)
.


% Whole rule: 
rule_34(Atr_1, Atr_2, Atr_3, _, Atr_5, Atr_6) :- 
	rule_34___thenClause(_, _, _, _, _, Atr_6) ; \+ (rule_34___ifClause(Atr_1, Atr_2, Atr_3, _, Atr_5, _))
.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 

% Rule name: rule_35 
% If-complexes: 
rule_35___if_1(Atr_1, Atr_2, Atr_3, Atr_4, _, _) :- 
	10.0 < Atr_1, Atr_1 =< 33.999,
	Atr_2 =< 2.999,
	Atr_3 =< 1.999,
	Atr_4 > 2.0
.


% if-clause: 
rule_35___ifClause(Atr_1, Atr_2, Atr_3, Atr_4, _, _) :- 
	rule_35___if_1(Atr_1, Atr_2, Atr_3, Atr_4, _, _)
.


% Then-complexes: 
rule_35___then_1(_, _, _, _, _, Atr_6) :- 
	member(Atr_6, ['f'])
.


% then-clause: 
rule_35___thenClause(_, _, _, _, _, Atr_6) :- 
	rule_35___then_1(_, _, _, _, _, Atr_6)
.


% Whole rule: 
rule_35(Atr_1, Atr_2, Atr_3, Atr_4, _, Atr_6) :- 
	rule_35___thenClause(_, _, _, _, _, Atr_6) ; \+ (rule_35___ifClause(Atr_1, Atr_2, Atr_3, Atr_4, _, _))
.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 

% Rule name: rule_36 
% If-complexes: 
rule_36___if_1(Atr_1, Atr_2, Atr_3, Atr_4, Atr_5, _) :- 
	74.0 < Atr_1, Atr_1 =< 118.999,
	Atr_2 =< 2.999,
	Atr_3 =< 2.999,
	Atr_4 > 1.0,
	1.0 < Atr_5, Atr_5 =< 3.999
.


% if-clause: 
rule_36___ifClause(Atr_1, Atr_2, Atr_3, Atr_4, Atr_5, _) :- 
	rule_36___if_1(Atr_1, Atr_2, Atr_3, Atr_4, Atr_5, _)
.


% Then-complexes: 
rule_36___then_1(_, _, _, _, _, Atr_6) :- 
	member(Atr_6, ['s'])
.


% then-clause: 
rule_36___thenClause(_, _, _, _, _, Atr_6) :- 
	rule_36___then_1(_, _, _, _, _, Atr_6)
.


% Whole rule: 
rule_36(Atr_1, Atr_2, Atr_3, Atr_4, Atr_5, Atr_6) :- 
	rule_36___thenClause(_, _, _, _, _, Atr_6) ; \+ (rule_36___ifClause(Atr_1, Atr_2, Atr_3, Atr_4, Atr_5, _))
.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 


