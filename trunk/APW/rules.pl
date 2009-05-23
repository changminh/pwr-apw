%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 

% Rule name: regula_nr_1 
% If-complexes: 
regula_nr_1___if_1(Atr_4) :- 
	Atr_4 =< 0.95
.


% if-clause: 
regula_nr_1___ifClause(Atr_4) :- 
	regula_nr_1___if_1(Atr_4)
.


% Then-complexes: 
regula_nr_1___then_1(Atr_5) :- 
	member(Atr_5, ['Iris-setosa'])
.


% then-clause: 
regula_nr_1___thenClause(Atr_5) :- 
	regula_nr_1___then_1(Atr_5)
.


% Whole rule: 
regula_nr_1(_, _, _, Atr_4, Atr_5) :- 
	regula_nr_1___thenClause(Atr_5) ; \+ (regula_nr_1___ifClause(Atr_4))
.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 

