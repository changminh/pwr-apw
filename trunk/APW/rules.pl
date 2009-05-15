%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 

% Rule name: regula_nr_1 
% If-complexes: 
regula_nr_1___if_1(Atr_4) :- 
	member(Atr_4, ['"Assoc-voc"', '"Some-college"'])
.


% if-clause: 
regula_nr_1___ifClause(Atr_4) :- 
	regula_nr_1___if_1(Atr_4)
.


% Then-complexes: 
regula_nr_1___then_1(Atr_14, Atr_15) :- 
	member(Atr_14, ['"Poland"']),
	member(Atr_15, ['">50K"'])
.


% then-clause: 
regula_nr_1___thenClause(Atr_14, Atr_15) :- 
	regula_nr_1___then_1(Atr_14, Atr_15)
.


% Whole rule: 
regula_nr_1(_, _, _, Atr_4, _, _, _, _, _, _, _, _, _, Atr_14, Atr_15) :- 
	regula_nr_1___thenClause(Atr_14, Atr_15) ; \+ (regula_nr_1___ifClause(Atr_4))
.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 

