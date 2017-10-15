parser grammar TurtleParser;

options {
  language = Java;
  tokenVocab = TurtleLexer;
}

@header {
  package TP1;
}

document returns [ASD.Document out]
  : s=List<sujet> EOF { $out = new ASD.Document($s.out); }
  ;

sujet returns [ASD.Document.Sujet out]
	:	e=entite; lp=List<predicat> {$out = new ASD.Document.Sujet(e.$out, lp.$out);}
	;

predicat:	(entite objet *);

objet:	objetTexte | objetEntite;

objetTexte:	texte;

objetEntite:	entite;

texte: ident ;

entite:	 ident ;

ident : ID;
