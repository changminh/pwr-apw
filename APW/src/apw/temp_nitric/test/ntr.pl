female(mary).
female(sandra).
female(juliet).
female(lisa).
male(peter).
male(paul).
male(dick).
male(bob).
male(harry).
parent(bob, lisa).
parent(bob, paul).
parent(bob, mary).
parent(juliet, lisa).
parent(juliet, paul).
parent(juliet, mary).
parent(peter, harry).
parent(lisa, harry).
parent(mary, dick).
parent(mary, sandra).

father(X, Y) :- parent(X, Y), male(X).
sister(X, Y) :- parent(Z, X), parent(Z, Y), female(X).
brother(X, Y) :- parent(Z, X), parent(Z, Y), male(X).
grandmother(X, Y) :- parent(X, Z), parent(Z, Y).
siblings(X, Y) :- sister(X, Y), X\=Y.
siblings(X, Y) :- brother(X, Y), X\=Y.
cousin(X, Y) :- parent(Z, X), parent(W, Y), siblings(Z, W).