/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Time;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Vlatko
 */
public class PatuvackiTrgovec extends Applet implements Runnable {

 protected int brojGradovi;
protected int goleminanaPopulacija;
protected double procentMutacija;
protected int mutirackaGolPopulacija;
protected int favoriziranaGolPopulacija;
protected int delbenaLinijaPozicija;
protected int generacija;
protected Thread nitka = null;
protected boolean zapocnata = false;
protected Grad [] gradovi;
protected Chromosome[] chromosomes;
private Button btnStart;
private Button btnPotrosenoVreme;
private TextField txtGradovi;
private TextField txtGoleminaPopulacija;
private TextField txtProcentMutacija;
private Panel Panela;
private String status = "";
public void main() {
}
    @Override
public void init() {
setLayout(new BorderLayout());

Panela = new Panel();

btnStart = new Button("Zapocni");
Panela.add(btnStart);
Panela.add(new Label("Broj na gradovi<50:"));
Panela.add(txtGradovi = new TextField(5));
Panela.add(new Label("Golemina na populacija:"));
Panela.add(txtGoleminaPopulacija = new TextField(5));
Panela.add(new Label(",Procent na mutacija:"));
Panela.add(txtProcentMutacija = new TextField(5));
btnPotrosenoVreme= new Button("Potroseno Vreme");
Panela.add(btnPotrosenoVreme);
this.add(Panela,BorderLayout.SOUTH);
txtGoleminaPopulacija.setText("1000");
txtProcentMutacija.setText("0.20");
txtGradovi.setText("30");
btnPotrosenoVreme.addActionListener(new ActionListener() {
public void actionPerformed(ActionEvent arg0) {

update();
}
});

btnStart.addActionListener(new ActionListener() {
public void actionPerformed(ActionEvent arg0) {

startThread();
}
});
zapocnata = false;

update();
}
public void startThread()  {
try {
brojGradovi = Integer.parseInt(txtGradovi.getText());


} catch (NumberFormatException e) {
brojGradovi = 50;
}
try {
goleminanaPopulacija = Integer.parseInt(txtGoleminaPopulacija.getText());
} catch (NumberFormatException e) {
goleminanaPopulacija = 1000;
}
try {
procentMutacija = new Double(txtProcentMutacija.getText())
.doubleValue();
} catch (NumberFormatException e) {
procentMutacija = 0;
}
FontMetrics fm = getGraphics().getFontMetrics();
int bottom = Panela.getBounds().y - fm.getHeight() - 2;
mutirackaGolPopulacija = goleminanaPopulacija / 2;
favoriziranaGolPopulacija = mutirackaGolPopulacija / 2;
delbenaLinijaPozicija = brojGradovi / 5;
gradovi = new Grad[brojGradovi];
for (int i = 0; i < brojGradovi; i++) {
gradovi[i] = new Grad(
(int) (Math.random() * (getBounds().width - 10)),
(int) (Math.random() * (bottom - 20)));
}
chromosomes = new Chromosome[goleminanaPopulacija];
for (int i = 0; i < goleminanaPopulacija; i++) {
chromosomes[i] = new Chromosome(gradovi);
chromosomes[i].setDelbena(delbenaLinijaPozicija);
chromosomes[i].setMutacija(procentMutacija);
}
Chromosome.sortirajChromosomes(chromosomes, goleminanaPopulacija);
zapocnata = true;
generacija = 0;
if (nitka != null)
nitka = null;

if(brojGradovi<51){
    nitka = new Thread(this);
nitka.setPriority(Thread.MIN_PRIORITY);
nitka.start();}
 else {setStatus("Max Dozvolen broj na gradovi e 50");
 update();
 }
}
public void update() {
Image img = createImage(getBounds().width, getBounds().height);
Graphics g = img.getGraphics();
FontMetrics fm = g.getFontMetrics();
int width = getBounds().width;
int bottom = Panela.getBounds().y - fm.getHeight() - 2;
g.setColor(Color.blue);
g.fillRect(0, 0, width, bottom);

if ((zapocnata && (gradovi != null))&& (brojGradovi<51)) {
g.setColor(Color.red);
for (int i = 0; i < brojGradovi; i++) {
int xpos = gradovi[i].getx();
int ypos = gradovi[i].gety();

g.fillRect(xpos - 5, ypos - 5, 10, 10);
}
g.setColor(Color.yellow);
for (int i = 0; i < brojGradovi; i++) {
int iGrad = chromosomes[0].getGrad(i);
if (i != 0) {
int posleden = chromosomes[0].getGrad(i - 1);
g.drawLine(gradovi[iGrad].getx(), gradovi[iGrad].gety(),gradovi[posleden].getx(), gradovi[posleden].gety());
}
}
}
g.setColor(Color.white);
g.drawString(status, 0, bottom);
getGraphics().drawImage(img, 0, 0, this);
}
public void setStatus(String status) {
this.status = status;
}
public void run() {
double tekovnaCena = 500.0;
double staraCena = 0.0;
double dCena = 500.0;
int brojIsti = 0;
long pocetok = 0;
long f, p;
Date vreme = new Date();
update();
while (brojIsti < 70) {
generacija++;
int iostatok = mutirackaGolPopulacija;
int mutirani = 0;

for (int i = 0; i < favoriziranaGolPopulacija; i++) {
Chromosome ChromosomeMajka = chromosomes[i];

int tatko = (int) (0.999999 * Math.random() * (double)
mutirackaGolPopulacija);
Chromosome ChromosomeTatko = chromosomes[tatko];
mutirani += ChromosomeMajka.mate(ChromosomeTatko, chromosomes[iostatok],
chromosomes[iostatok + 1]);
iostatok += 2;
}

for (int i = 0; i < mutirackaGolPopulacija; i++) {
chromosomes[i] = chromosomes[i + mutirackaGolPopulacija];
chromosomes[i].presmetajCena(gradovi);
}

Chromosome.sortirajChromosomes(chromosomes, mutirackaGolPopulacija);
double cena = chromosomes[0].getCena();
dCena = Math.abs(cena - tekovnaCena);
tekovnaCena = cena;
double mutationRate = 100.0 * (double) mutirani/ (double) mutirackaGolPopulacija;
NumberFormat nf = NumberFormat.getInstance();
nf.setMinimumFractionDigits(2);
nf.setMinimumFractionDigits(2);
pocetok = vreme.getTime();
SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
Date now = new Date();
String strTime = sdfTime.format(now);
setStatus("Pocetno vreme:" + strTime + " Generacija: " + generacija +
" Cena" + (int) tekovnaCena+ " Mutacii " + nf.format(mutationRate) + "%");
if ((int) tekovnaCena == (int) staraCena) {
brojIsti++;
} else {
brojIsti = 0;
staraCena = tekovnaCena;
}
update();
}

f = System.currentTimeMillis();
p = f - pocetok;
SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
Date now = new Date();
String strTime = sdfTime.format(now);
setStatus( "Reshenieto e pronajdeno vo" + generacija + " generacija." +" Vreme na zavrshuvanje:" + strTime + "Potroseno vreme " + p + "millisekundi");

}
    @Override
public void paint(Graphics g) {
update();
}
}