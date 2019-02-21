/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Vlatko
 */
class Chromosome {
protected int [] listaNaGradovi;
protected double cena;
protected double procentMutacija;
protected int delbenaLinijaPozicija;

Chromosome( Grad[] gradovi) {
boolean zafatenostGrad[] = new boolean[gradovi.length];
listaNaGradovi = new int[gradovi.length];
cena = 0.0;
for ( int i=0;i<listaNaGradovi.length;i++ ) zafatenostGrad[i] = false;
for ( int i=0;i<listaNaGradovi.length-1;i++ ) {
int gradKandidat;
do {
gradKandidat = (int) ( 0.999999* Math.random() * (double) listaNaGradovi.length );
} while ( zafatenostGrad[gradKandidat] );
listaNaGradovi[i] = gradKandidat;
zafatenostGrad[gradKandidat] = true;
if ( i == listaNaGradovi.length-2 ) {
gradKandidat = 0;
while ( zafatenostGrad[gradKandidat] ) gradKandidat++;
listaNaGradovi[i+1] = gradKandidat;
}
}
presmetajCena(gradovi);
delbenaLinijaPozicija = 1;
}

void presmetajCena(Grad [] cities) {
cena=0;
for ( int i=0;i<listaNaGradovi.length-1;i++ ) {
double rast = cities[listaNaGradovi[i]].rastojanie(cities[listaNaGradovi[i+1]]);
cena += rast;
}
}
double getCena() {
return cena;
}
int getGrad(int i) {
return listaNaGradovi[i];
}
void setGradovi(int [] lista) {
for ( int i=0;i<listaNaGradovi.length;i++ ) {
listaNaGradovi[i] = lista[i];
}
}
void setGrad(int index, int value) {
listaNaGradovi[index] = value;
}
void setDelbena(int poz) {
delbenaLinijaPozicija = poz;
}
void setMutacija(double verojatnost) {
procentMutacija = verojatnost;
}
int mate(Chromosome tatko, Chromosome potomok1, Chromosome potomok2) {
int delbenapozicija1 = (int) (0.999999* Math.random() *(double)(listaNaGradovi.length-delbenaLinijaPozicija));
int delbenapozicija2 = delbenapozicija1 + delbenaLinijaPozicija;
boolean pominati1 [] = new boolean[listaNaGradovi.length];
boolean pominati2 [] = new boolean[listaNaGradovi.length];
int ostatok1 [] = new int[listaNaGradovi.length];
int ostatok2 [] = new int[listaNaGradovi.length];
for ( int i=0;i<listaNaGradovi.length;i++ ) {

pominati1[i] = false;
pominati2[i] = false;
}
for ( int i=0;i<listaNaGradovi.length;i++ ) {
if ( i<delbenapozicija1 || i>= delbenapozicija2 ) {
ostatok1[i] = -1;
ostatok2[i] = -1;
} else {
int iMajka = listaNaGradovi[i];
int iTatko = tatko.getGrad(i);
ostatok1[i] = iTatko;
ostatok2[i] = iMajka;
pominati1[iTatko] = true;
pominati2[iMajka] = true;
}
}
for ( int i=0;i<delbenapozicija1;i++ ) {
if ( ostatok1[i] == -1 ) {
for ( int j=0;j<listaNaGradovi.length;j++ ) {
int iMajka = listaNaGradovi[j];
if ( !pominati1[iMajka] ) {
ostatok1[i] = iMajka;
pominati1[iMajka] = true;
break;
}
}
}
if ( ostatok2[i] == -1 ) {
for ( int j=0;j<listaNaGradovi.length;j++ ) {
int iTatko = tatko.getGrad(j);
if ( !pominati2[iTatko] ) {
ostatok2[i] = iTatko;
pominati2[iTatko] = true;
break;
}
}
}
}
for ( int i=listaNaGradovi.length-1;i>=delbenapozicija2;i-- ) {
if ( ostatok1[i] == -1 ) {
for ( int j=listaNaGradovi.length-1;j>=0;j-- ) {
int iMajka = listaNaGradovi[j];
if ( !pominati1[iMajka] ) {
ostatok1[i] = iMajka;
pominati1[iMajka] = true;
break;
}
}
}
if ( ostatok2[i] == -1 ) {
for ( int j=listaNaGradovi.length-1;j>=0;j-- ) {
int iTatko = tatko.getGrad(j);
if ( !pominati2[iTatko] ) {
ostatok2[i] = iTatko;
pominati2[iTatko] = true;
break;
}
}
}
}
potomok1.setGradovi(ostatok1);
potomok2.setGradovi(ostatok2);
int mutaciibroj = 0;
if ( Math.random() < procentMutacija ) {
int zamena1 = (int) (0.999999* Math.random() *(double)(listaNaGradovi.length));
int zamena2 = (int) (0.999999* Math.random() *(double)listaNaGradovi.length);
int i = ostatok1[zamena1];
ostatok1[zamena1] = ostatok1[zamena2];
ostatok1[zamena2] = i;
mutaciibroj++;
}
if ( Math.random() < procentMutacija ) {
int zamena1 = (int) (0.999999* Math.random() *(double)(listaNaGradovi.length));
int zamena2 = (int) (0.999999* Math.random() *(double)listaNaGradovi.length);
int i = ostatok2[zamena1];
ostatok2[zamena1] = ostatok2[zamena2];
ostatok2[zamena2] = i;
mutaciibroj++;
}
return mutaciibroj;
}
public static void sortirajChromosomes(Chromosome chromosomes[],int vkupno) {
Chromosome pomosno;
boolean zameneti = true;
while ( zameneti ) {
zameneti = false;
for ( int i=0;i<vkupno-1;i++ ) {
if ( chromosomes[i].getCena() > chromosomes[i+1].getCena() ) {
pomosno = chromosomes[i];
chromosomes[i] = chromosomes[i+1];
chromosomes[i+1] = pomosno;
zameneti = true;
}
}
}
}
}