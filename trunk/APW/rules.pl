%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 

% Rule name: regula_nr_1 
% If-complexes: 
regula_nr_1___if_1(_, _, _, Atr_4, _) :- 
	Atr_4 =< 0.2
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

% Rule name: regula_nr_2 
% If-complexes: 
regula_nr_2___if_1(_, _, _, Atr_4, _) :- 
	Atr_4 =< 0.5
.


% if-clause: 
regula_nr_2___ifClause(_, _, _, Atr_4, _) :- 
	regula_nr_2___if_1(_, _, _, Atr_4, _)
.


% Then-complexes: 
regula_nr_2___then_1(_, _, _, _, Atr_5) :- 
	member(Atr_5, ['Iris-setosa'])
.


% then-clause: 
regula_nr_2___thenClause(_, _, _, _, Atr_5) :- 
	regula_nr_2___then_1(_, _, _, _, Atr_5)
.


% Whole rule: 
regula_nr_2(_, _, _, Atr_4, Atr_5) :- 
	regula_nr_2___thenClause(_, _, _, _, Atr_5) ; \+ (regula_nr_2___ifClause(_, _, _, Atr_4, _))
.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 

% Rule name: regula_nr_3 
% If-complexes: 
regula_nr_3___if_1(_, _, _, Atr_4, _) :- 
	Atr_4 =< 0.96
.


% if-clause: 
regula_nr_3___ifClause(_, _, _, Atr_4, _) :- 
	regula_nr_3___if_1(_, _, _, Atr_4, _)
.


% Then-complexes: 
regula_nr_3___then_1(_, _, _, _, Atr_5) :- 
	member(Atr_5, ['Iris-setosa'])
.


% then-clause: 
regula_nr_3___thenClause(_, _, _, _, Atr_5) :- 
	regula_nr_3___then_1(_, _, _, _, Atr_5)
.


% Whole rule: 
regula_nr_3(_, _, _, Atr_4, Atr_5) :- 
	regula_nr_3___thenClause(_, _, _, _, Atr_5) ; \+ (regula_nr_3___ifClause(_, _, _, Atr_4, _))
.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 

