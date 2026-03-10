### ***Colato Daniel - 4CI - anno scolastico 2025/26*** 

# **LABORATORIO DI INFORMATICA**

### 4 febbraio 2026

## Appunti e codice lezione del 4/02/26
<p>Appunti:</p><ul>
<li><b>JFrame</b>: Classe padre che permette di creare oggetti per un'interfaccia (sarà necessario usare extends)</li>
<li><b>Container</b>: con il metodo getContentPane() permette di creare una griglia in cui inserire oggetti in ordine</li>
<li><b>JButton</b>: permette di creare un pulsante</li>
<li><b>JTextField</b>: permette di creare una casella di testo in cui l'utente può inserire stringhe</li>
<li><b>JPanel</b>: permette di creare un pannello</li>
<li><b>ActionListener</b>: implementazione di una classe Ascoltatore che permette di capire quale oggetto ha provocato un evento</li>
</ul>

<p>Codice:</p>

```java
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

    public class Calcolatrice extends JFrame{
        //attributi
        private Container c;
        private JButton numeri[];
        private JTextField testo;

        private String s="";
        private int primoOperando=0;
        private String operazione="";

        private JPanel contenitore;
    
        private JPanel pannello1;
        private JPanel pannello2;
        //costruttore
        public Calcolatrice(String nome){
            super(nome);
            c=new Container();
            testo=new JTextField(20);
            c=this.getContentPane();
            this.setSize(500, 500);
            JButton b;
            contenitore=new JPanel(new GridLayout(1, 2, 2, 2));
            pannello1=new JPanel();
            pannello1.setBackground(Color.red);
            pannello2=new JPanel();
            pannello2.setBackground(Color.BLUE);
            contenitore.add(pannello1);
            contenitore.add(pannello2);
            pannello1.add(testo);
            numeri=new JButton[10];
            for (int i=0; i<10; i++){
                b=new JButton("" + i);
                numeri[i]=b;
                pannello2.add(numeri[i]);
                numeri[i].addActionListener(new Ascoltatore()); 
            }
            // Bottoni operazioni
            JButton btnSomma=new JButton("+");
            pannello2.add(btnSomma);
            btnSomma.addActionListener(new Ascoltatore());
            
            JButton btnSottrazione=new JButton("-");
            pannello2.add(btnSottrazione);
            btnSottrazione.addActionListener(new Ascoltatore());
            
            JButton btnMoltiplicazione=new JButton("*");
            pannello2.add(btnMoltiplicazione);
            btnMoltiplicazione.addActionListener(new Ascoltatore());
            
            JButton btnDivisione=new JButton("/");
            pannello2.add(btnDivisione);
            btnDivisione.addActionListener(new Ascoltatore());
            
            JButton btnUguale=new JButton("=");
            pannello2.add(btnUguale);
            btnUguale.addActionListener(new Ascoltatore());
            
            c.add(contenitore);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setVisible(true);
        }
        //implementazione del metodo
        public class Ascoltatore implements ActionListener{
            public void actionPerformed(ActionEvent e){
                String comando=e.getActionCommand();
                // Se è un numero
                if(comando.matches("[0-9]")){
                    s+=comando;
                    if(operazione.equals("")){
                        testo.setText(s);
                    }else{
                        testo.setText(primoOperando + " " + operazione + " " + s);
                    }
                }else if(comando.equals("+") || comando.equals("-") || comando.equals("*") || comando.equals("/")){
                    // Se è un'operazione
                    primoOperando=Integer.parseInt(s);
                    operazione=comando;
                    testo.setText(primoOperando + " " + operazione);
                    s="";
                }else if(comando.equals("=")){
                    // Se è uguale
                    int secondoOperando=Integer.parseInt(s);
                    int risultato=0;
                    if(operazione.equals("+")){
                        risultato=primoOperando+secondoOperando;
                    }else if(operazione.equals("-")){
                        risultato=primoOperando-secondoOperando;
                    }else if(operazione.equals("*")){
                        risultato=primoOperando*secondoOperando;
                    }else if(operazione.equals("/")){
                        risultato=primoOperando/secondoOperando;
                    }
                    testo.setText(""+risultato);
                    s=""+risultato;
                    operazione="";
                }
            }
        }
        public static void main(String[] args) {
            new Calcolatrice("Calcolatrice");
        }
    }
```
### 11 febbraio 2026

## Appunti di NetBeans e codice lezione del 11/02/26 (Navigator)
<p>Appunti:</p><ul>
<li><b>Creazione del progetto</b>: Una volta aperta l'app NetBeans, cliccare in alto a sinistra su "File"-->"New Project", selezionare come categoria "Java with Maven", come Projects "Java Application", modificare il nome del progetto, eventualmente cambiare il path, premere "Finish"</li>
<li><b>Compilazione</b>: la compilazione è automatica ed istantanea, per eseguire il codice fare click dx sul file con estensione .java nel package "<i>com.mycompany...</i>" e selezionare "Run File"</li>
<li><b>Modifica del design</b>: nella sezione "Design", nella casella a destra denominata "Palette", è possibile trascinare vari oggetti nell'intefaccia al centro, ad esempio "Panel", "Label", "Text field" e "Button", modificarne le proprietà e le dimensioni. Per aggiungere eventi, cliccare con il tasto dx sull'oggetto, selezionare "Events" e aggiungere ciò che è necessario</li>
<li><b>Modifica del source</b>: nella sezione "Source", è possibile modificare il codice e aggiungere metodi come .set alle variabili d'istanza create</li>
</ul>

Codice:
```java
    package com.mycompany.navigator;
    public class Navigator1 extends javax.swing.JFrame {
    /**
     * Creates new form Navigator1
     */
    private int vett[] = {1,2,3,4,5};
    private int pos=0;
    
    public Navigator1() {
        initComponents();
    }                                                         

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {                                      
        // TODO add your handling code here:
        if(pos==0){
            jTextField1.setText(""+vett[0]);
        }else if (pos>0){
            pos--;
            jTextField1.setText(""+vett[pos]);
        }
    }                                     

    private void jButton2MouseClicked(java.awt.event.MouseEvent evt) {                                      
        // TODO add your handling code here:
        if(pos>=vett.length){
            jTextField1.setText(""+vett[vett.length-1]);
        }else if(pos<vett.length-1){
            pos++;
            jTextField1.setText(""+vett[pos]);
        }
    }                                     

    // Variables declaration - do not modify                     
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration                   
}
```


### 25 febbraio 2026

## Stesura delle prime caratteristiche del gioco
<p>Stesura su carta delle caratteristiche del gioco, inizio della creazione delle classi UML ed elenco delle formule fisiche da utilizzare.<br><br>

Appunti sulle caratteristiche del gioco (velivolo e scenario):
</p>
<img src="https://github.com/user-attachments/assets/a296c72c-940e-4f0a-a888-7da67044f4a6" alt="Caratteristiche gioco"  width=500>
<br><br>
<p>
Esempio classe UML:
</p>
<img src="https://github.com/user-attachments/assets/fd552ae8-b98f-42a1-aa0c-e6e0416b7aaa" alt="Immagine di esempio UML" width="300">

### 4 marzo 2026

## Creazione dell'architettura logica

<p>Ho iniziato a realizzare le nozioni base della logica per il funzionamento del gioco, tra cui l'assegnazione dei tasti della tastiera ai relativi comandi, la stesura dei dialoghi tra pilota e torre di controllo e l'elenco delle relative formule fisiche da utilizzare.</p><br>
Ho inoltre cercato e trovato su internet alcuni modelli 3d che daranno la base del nostro aereo.<br><br>
Esempio di dialoghi con torre di controllo:
<img src="https://github.com/user-attachments/assets/2795dfc7-078d-4224-a9b7-6cdbe2ccfbe7" alt="Immagine dialoghi torre di controllo" width="400">