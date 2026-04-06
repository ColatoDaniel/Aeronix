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
        public class Ascoltatore implements ActionListener{
            public void actionPerformed(ActionEvent e){
                String comando=e.getActionCommand();
                if(comando.matches("[0-9]")){
                    s+=comando;
                    if(operazione.equals("")){
                        testo.setText(s);
                    }else{
                        testo.setText(primoOperando + " " + operazione + " " + s);
                    }
                }else if(comando.equals("+") || comando.equals("-") || comando.equals("*") || comando.equals("/")){
                    primoOperando=Integer.parseInt(s);
                    operazione=comando;
                    testo.setText(primoOperando + " " + operazione);
                    s="";
                }else if(comando.equals("=")){
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
    private int vett[] = {1,2,3,4,5};
    private int pos=0;
    
    public Navigator1() {
        initComponents();
    }                                                         

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {                                      
        if(pos==0){
            jTextField1.setText(""+vett[0]);
        }else if (pos>0){
            pos--;
            jTextField1.setText(""+vett[pos]);
        }
    }                                     

    private void jButton2MouseClicked(java.awt.event.MouseEvent evt) {                                      
        if(pos>=vett.length){
            jTextField1.setText(""+vett[vett.length-1]);
        }else if(pos<vett.length-1){
            pos++;
            jTextField1.setText(""+vett[pos]);
        }
    }                                     

    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextField1;
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

### 11 marzo 2026

## Identificazione delle forze in gioco e prima struttura della classe Aereo


Le quattro forze principali che agiscono su un aereo in volo sono: **Portanza (L)** perpendicolare al vettore velocità generata dalle ali, **Resistenza aerodinamica (D)** opposta al moto, **Spinta (T)** generata dal motore e **Peso (W)** forza gravitazionale verso il basso con W = m·g.

Ho creato la struttura base della classe `Aereo`, che sarà il cuore del model fisico:

```java
public class Airplane {
    // Parametri fisici del velivolo (costanti)
    private double massa;            // kg
    private double superficieAlare;  // m²
    private double spintaMax;        // N
    private double throttle;         // 0.0 - 1.0

    // Stato cinematico (cambia ad ogni frame)
    private double velocita;         // m/s
    private double quota;            // m sul livello del mare
    private double angoloPitch;      // radianti (beccheggio)
    private double angoloBanco;      // radianti (rollio)

    // Forze calcolate
    private double portanza;
    private double resistenza;
    private double spinta;
    private double peso;
}
```


### 12 marzo 2026

## Formula della portanza e primo metodo calcolaPortanza()

La formula della portanza è **L = ½ · ρ · v² · S · CL** dove ρ è la densità dell'aria, v la velocità, S la superficie alare e CL il coefficiente di portanza. CL dipende dall'**angolo d'attacco** (α): l'angolo tra la direzione del vento relativo e la corda alare. Per piccoli angoli vale approssimativamente CL ≈ 2π·α.

```java
private double calcolaPortanza(double rho, double vel, double cl) {
    // L = 0.5 * rho * v^2 * S * CL
    return 0.5 * rho * vel * vel * superficieAlare * cl;
}
```

Ho anche scritto il metodo per ricavare CL dall'angolo d'attacco, con gestione dello **stallo**: condizione in cui α supera il valore critico (~15°) e CL crolla bruscamente, causando perdita di portanza:

```java
private double calcolaCL(double alfa) {
    double alfaStallo = Math.toRadians(15.0);
    double clMax = 1.5;
    if (alfa < alfaStallo) {
        return clMax * (alfa / alfaStallo); // zona lineare
    } else {
        return clMax * Math.max(0, 1 - (alfa - alfaStallo) * 5); // stallo
    }
}
```

### 13 marzo 2026

## Resistenza aerodinamica e peso

La resistenza si calcola in modo analogo alla portanza: **D = ½ · ρ · v² · S · CD**. CD cresce quando CL cresce perché le ali che generano portanza creano anche resistenza indotta. Ho usato la formula: **CD = CD0 + k · CL²** dove CD0 è la resistenza parassita (fusoliera, carrello) e k·CL² è la resistenza indotta.

```java
private double calcolaResistenza(double rho, double vel, double cl) {
    double cd0 = 0.02;  // resistenza parassita
    double k   = 0.05;  // fattore resistenza indotta
    double cd  = cd0 + k * cl * cl;
    return 0.5 * rho * vel * vel * superficieAlare * cd;
}

private double calcolaPeso() {
    return massa * 9.81; // W = m * g
}
```

### 16 marzo 2026

## Modello atmosfera ISA: densità dell'aria in funzione della quota

La densità dell'aria non è costante ma diminuisce con la quota. Questo è fondamentale: sia portanza che resistenza dipendono da ρ, quindi a quote più alte l'aria è meno densa e il velivolo fatica a sostenersi.

Uso il modello **ISA (Atmosfera Standard Internazionale)** con l'approssimazione valida in troposfera (0–11.000 m): **ρ(h) ≈ ρ₀ · (1 − 0.0000226 · h)^4.256** con ρ₀ = 1.225 kg/m³.

```java
public class AtmosferaISA {
    private static final double RHO0 = 1.225; // kg/m³ al livello del mare

    public static double getDensita(double quota) {
        if (quota < 0) return RHO0;
        if (quota <= 11000) {
            // troposfera
            return RHO0 * Math.pow(1 - 0.0000226 * quota, 4.256);
        } else {
            // stratosfera (semplificato)
            return 0.364 * Math.exp(-0.0001577 * (quota - 11000));
        }
    }
}
```

Ho creato una classe statica separata così basta chiamare `AtmosferaISA.getDensita(quota)` da qualunque punto del codice senza duplicare la formula.


### 17 marzo 2026

## Equazioni del moto e integrazione di Eulero

Trattando il velivolo come punto materiale le equazioni del moto sui due assi sono:

**Asse x:** a_x = (T·cos(θ) − D·cos(θ) − W·sin(θ)) / m

**Asse z:** a_z = (L − W·cos(θ) + T·sin(θ) − D·sin(θ)) / m

Per aggiornare velocità e posizione uso l'**integrazione di Eulero**: v(t+Δt) = v(t) + a(t)·Δt e x(t+Δt) = x(t) + v(t)·Δt. È il metodo più semplice e con Δt ≈ 0.016 s (60 fps) è sufficientemente preciso.

```java
public void update(Aereo aereo, double dt) {
    double rho = AtmosferaISA.getDensita(aereo.getQuota());
    double cl  = calcolaCL(aereo.getAngoloPitch());
    double l   = calcolaPortanza(rho, aereo.getVelocita(), cl);
    double d   = calcolaResistenza(rho, aereo.getVelocita(), cl);
    double t   = aereo.getThrottle() * aereo.getSpintaMax();
    double w   = aereo.getMassa() * 9.81;
    double p   = aereo.getAngoloPitch();

    double ax = (t * Math.cos(p) - d * Math.cos(p) - w * Math.sin(p)) / aereo.getMassa();
    double az = (l - w * Math.cos(p) + t * Math.sin(p) - d * Math.sin(p)) / aereo.getMassa();

    // integrazione di Eulero
    aereo.setVelocita(aereo.getVelocita() + ax * dt);
    aereo.setQuota(aereo.getQuota() + aereo.getVelocita() * Math.sin(p) * dt);
}
```

### 18 marzo 2026

## Gestione della spinta e consumo carburante

Il **throttle** è un valore tra 0.0 e 1.0 che rappresenta la percentuale di potenza del motore. La spinta è T = throttle · spintaMax. Il carburante diminuisce in proporzione: fc = throttle · consumoMax (kg/s). Quando si esaurisce la spinta scende a zero e l'aereo diventa un aliante.

```java
private double carburante;
private double carburanteMax;
private double consumoMax;
private boolean motoreAcceso = true;

public void aggiornaSistemaMotore(double dt) {
    if (!motoreAcceso) { spinta = 0; return; }

    spinta = throttle * spintaMax;
    double consumo = throttle * consumoMax * dt;
    carburante = Math.max(0, carburante - consumo);

    if (carburante <= 0) {
        motoreAcceso = false;
        System.out.println("CARBURANTE ESAURITO");
    }
}
```

### 19 marzo 2026

## Gestione degli input da tastiera con InputManager

Ho creato una classe `InputManager` separata per gestire gli input. Tiene un array di booleani: ogni tasto ha un flag che diventa `true` quando premuto e `false` quando rilasciato. Così l'aggiornamento degli angoli è continuo e fluido ad ogni frame invece di reagire a impulsi singoli.

```java
public class InputManager {
    private boolean[] tasti = new boolean[256];

    public void setTasto(int keyCode, boolean stato) {
        if (keyCode >= 0 && keyCode < tasti.length)
            tasti[keyCode] = stato;
    }

    public boolean isTastoPremuto(int keyCode) {
        return tasti[keyCode];
    }
}
```

Nel metodo `aggiorna()` leggo i flag e modifico gli angoli:

```java
// W/S = pitch su/giù
if (input.isTastoPremuto(KeyEvent.VK_W)) angoloPitch += 0.01;
if (input.isTastoPremuto(KeyEvent.VK_S)) angoloPitch -= 0.01;
// A/D = bank angle (rollio)
if (input.isTastoPremuto(KeyEvent.VK_A)) angoloBanco -= 0.01;
if (input.isTastoPremuto(KeyEvent.VK_D)) angoloBanco += 0.01;
// frecce = throttle
if (input.isTastoPremuto(KeyEvent.VK_UP))   throttle = Math.min(1.0, throttle + 0.01);
if (input.isTastoPremuto(KeyEvent.VK_DOWN)) throttle = Math.max(0.0, throttle - 0.01);
```

### 20 marzo 2026

## Limiti fisici agli angoli e clamp della velocità

Non tutti gli angoli sono fisicamente raggiungibili. Ho aggiunto dei limiti: pitch tra −45° e +45°, bank angle tra −90° e +90°, velocità tra 0 e ~290 m/s (Mach 0.85). Il **clamp** è un'operazione che forza un valore a stare tra un minimo e un massimo.

```java
private void applicaLimiti() {
    double pitchMax = Math.toRadians(45.0);
    double bankMax  = Math.toRadians(90.0);
    double velMax   = 290.0; // m/s

    angoloPitch = Math.max(-pitchMax, Math.min(pitchMax, angoloPitch));
    angoloBanco = Math.max(-bankMax,  Math.min(bankMax,  angoloBanco));
    velocita    = Math.max(0.0,       Math.min(velMax,   velocita));

    // la quota non può andare sotto zero
    if (quota < 0) { quota = 0; velocita = 0; }
}
```

Questo metodo viene chiamato alla fine di ogni ciclo `update()`.

### 21 marzo 2026

## Approfondimento sulla classe Vector3 e operazioni vettoriali


Ho passato oggi a studiare le operazioni vettoriali necessarie per la fisica 3D. Nel nostro progetto la classe `Vector3` contiene tutte le operazioni statiche di utilità. Le più importanti sono il **prodotto scalare** (dot product) e il **prodotto vettoriale** (cross product):

**Prodotto scalare**: a · b = ax·bx + ay·by + az·bz → risultato scalare, indica quanto due vettori sono allineati (= 0 se perpendicolari, > 0 stesso verso, < 0 versi opposti).

**Prodotto vettoriale**: a × b → vettore perpendicolare al piano dei due vettori, usato per calcolare le normali ai triangoli e la portanza.

```java
public static double dotProduct(Vector3 a, Vector3 b) {
    return a.x*b.x + a.y*b.y + a.z*b.z;
}

public static Vector3 crossProduct(Vector3 a, Vector3 b) {
    return new Vector3(
        a.y*b.z - a.z*b.y,
        a.z*b.x - a.x*b.z,
        a.x*b.y - a.y*b.x
    );
}
```

Ho trovato utile il video *"3D Math for Game Developers"* su YouTube (canale **GDC**) per ripassare questi concetti prima di implementarli nel modello fisico.

### 23 marzo 2026

## Virata coordinata: scomposizione della portanza con il banco


Quando il velivolo è in virata con angolo di banco φ la portanza non è più tutta verticale. Si scompone in: **Lv = L · cos(φ)** che sostiene il peso, e **Lh = L · sin(φ)** che genera la forza centripeta per curvare. In virata serve quindi più portanza per mantenere quota, altrimenti l'aereo scende.

```java
public double getPortanzaVerticale() {
    return portanza * Math.cos(angoloBanco);
}

public double getForzaCentripeta() {
    return portanza * Math.sin(angoloBanco);
}
```

Ho aggiornato il metodo `aggiorna()` usando `getPortanzaVerticale()` invece di `portanza` direttamente nel calcolo di az, così la fisica della virata è corretta.

### 24 marzo 2026

## Velocità di stallo e rilevamento della condizione di stallo


La **velocità di stallo** è la velocità minima al di sotto della quale non si genera portanza sufficiente a sostenere il peso: **Vs = √(2·W / ρ·S·CL_max)**

```java
public double calcolaVelocitaStallo(double rho) {
    double clMax = 1.5;
    return Math.sqrt((2 * calcolaPeso()) / (rho * superficieAlare * clMax));
}

public boolean isInStallo(double rho) {
    return velocita < calcolaVelocitaStallo(rho);
}
```

Quando il velivolo entra in stallo il muso tende a cadere verso il basso (comportamento reale):

```java
if (isInStallo(rho)) {
    angoloPitch = Math.max(angoloPitch - 0.005, Math.toRadians(-20));
    quota -= 2.0 * dt; // perdita di quota accelerata
}
```

### 25 marzo 2026

## Refactoring: separazione del codice in più classi

`Aereo.java` stava diventando troppo lungo. Ho diviso i vari compiti nelle varie classi e ho creato:
- `Aereo.java` → stato del velivolo (posizione, angoli, velocità)
- `MotoreFisico.java` → tutti i calcoli delle forze
- `InputManager.java` → input da tastiera
- `AtmosferaISA.java` → modello dell'atmosfera
- `Costanti.java` → costanti fisiche

```java
public class Costanti {
    public static final double G     = 9.81;   // m/s²
    public static final double RHO0  = 1.225;  // kg/m³
    public static final double MACH1 = 340.0;  // m/s velocità del suono
}
```

Usare costanti con nome esplicito rende il codice molto più leggibile rispetto a scrivere `9.81` o `1.225` direttamente nelle formule.

### 26 marzo 2026

## Metodo update() definitivo con tutte le forze integrate

Appunti:

Ho finalmente messo insieme tutto nel metodo `update()` completo di `MotoreFisico`:

```java
public void update(Aereo aereo, double dt) {
    double quota = aereo.getQuota();
    double vel   = aereo.getVelocita();
    double pitch = aereo.getAngoloPitch();
    double bank  = aereo.getAngoloBanco();

    double rho = AtmosferaISA.getDensita(quota);
    double cl  = calcolaCL(pitch);
    double l   = calcolaPortanza(rho, vel, cl);
    double d   = calcolaResistenza(rho, vel, cl);
    double t   = aereo.getThrottle() * aereo.getSpintaMax();
    double w   = aereo.getMassa() * Costanti.G;

    // portanza scomposta per il banco
    double lv = l * Math.cos(bank);

    double ax = (t - d) / aereo.getMassa();
    double az = (lv - w) / aereo.getMassa();

    aereo.setVelocita(vel + ax * dt);
    aereo.setQuota(quota + vel * Math.sin(pitch) * dt);

    aereo.applicaLimiti();
    aereo.aggiornaSistemaMotore(dt);

    if (aereo.isInStallo(rho)) {
        aereo.setAngoloPitch(pitch - 0.005);
    }
}
```

### 27 marzo 2026

## Approfondimento su Transform e Matrix3x3: rotazioni 3D nel progetto


Ho studiato più a fondo come funzionano le rotazioni 3D, grazie a Youtube e Claude. La classe `Transform` mantiene tre vettori ortogonali (`forward`, `up`, `right`) che definiscono l'orientamento del GameObject. Ogni volta che si chiama `setPitch`, `setYaw` o `setRoll`, il trasformatore costruisce una `Matrix3x3` di rotazione attorno all'asse corrispondente usando la formula di Rodrigues:

```java
public void setPitch(double angle) {
    Matrix3x3 m = Matrix3x3.axisAngleMatrix(right, angle - rotation.x);
    up      = Vector3.applyMatrix(m, up);
    forward = Vector3.applyMatrix(m, forward);
    if (gameObject.getMesh() != null) gameObject.getMesh().rotate(m, position);
    rotation.x = angle;
}
```

**Matrice di rotazione**: una matrice 3×3 che trasforma un vettore ruotandolo di un certo angolo attorno a un asse. Il vantaggio rispetto agli angoli di Eulero separati è che le matrici si compongono con la moltiplicazione, evitando problemi di ordine di applicazione.

### 28 marzo 2026

## Studio della classe AirplaneModel: come il modello fisico gestisce velocità e forze

Ho scritto la classe `AirplaneModel.java` che è il cuore della fisica del progetto finale. A differenza del mio modello semplificato con angoli espliciti, questo usa un approccio basato su vettori e forze applicate alla velocità direttamente. Le forze principali applicate ad ogni tick:

- **`applyGravity()`**: aggiunge `(0, -gravity * deltaTime, 0)` alla velocità — semplicissimo
- **`applyLift()`**: usa il cross product tra velocità e vettore `right` del velivolo per trovare la direzione della portanza, poi la scala per `forwardSpeed²`
- **`applyDrag()`**: riduce la velocità proporzionalmente alla sua magnitudine, e aggiunge una componente di resistenza laterale

```java
private void applyLift() {
    addForce(Vector3.multiply(
        Vector3.crossProduct(velocity, getTransform().getRight()).getNormalized(),
        Math.min(2000, forwardSpeed * forwardSpeed * liftCoefficient * deltaTime * altitudeFactor(1))));
}
```

La funzione `altitudeFactor` riduce portanza e resistenza all'aumentare della quota simulando la minor densità dell'aria:

```java
private double altitudeFactor(double extraBase) {
    double f = 2000 / (physicsPosition.y - groundLevel + 2000) + extraBase;
    return f * f * f;
}
```

Il cubo di `f` fa sì che l'effetto sia molto pronunciato ad alta quota. Ho verificato che questo è coerente con la legge ISA che prevede una riduzione esponenziale della densità.

### 29 marzo 2026

## Effetto aerodinamico e drag angolare

Ho realizzato i metodi `applyAerodynamicEffect()` e `applyAngularDrag()` di `AirplaneModel`. L'effetto aerodinamico è il comportamento per cui il velivolo tende ad allinearsi con la propria direzione di volo, come una freccia: se ha una componente di velocità laterale, questa viene gradualmente ridotta e il muso ruota per seguire la traiettoria reale.

```java
private void applyAerodynamicEffect() {
    if (velocity.getSqrMagnitude() > 0) {
        double cf = Vector3.dotProduct(getTransform().getForward(), velocity.getNormalized());
        cf *= cf;
        velocity = Vector3.lerp(velocity,
            Vector3.projectToVector(velocity, getTransform().getForward()),
            cf * forwardSpeed * aerodynamicEffect * deltaTime / 2);
        // aggiorna anche la rotazione del muso
    }
}
```

**`lerp`** (Linear Interpolation): funzione che calcola un punto intermedio tra due valori in base a un parametro t ∈ [0,1]. `lerp(a, b, t) = a + (b − a) · t`. Con t piccolo il risultato è vicino ad a, con t = 1 coincide con b.

Il drag angolare `applyAngularDrag()` invece frena la rotazione del velivolo ad ogni frame, impedendo che l'aereo continui a girare indefinitamente dopo che si smette di premere i tasti:

```java
private void applyAngularDrag() {
    velocityPitch -= velocityPitch * angularDragCoef * deltaTime;
    velocityYaw   -= velocityYaw   * angularDragCoef * deltaTime;
    velocityRoll  -= velocityRoll  * angularDragCoef * deltaTime;
}
```

### 30 marzo 2026

## Collegamento con la parte grafica

Ho discusso con Gioele come far comunicare il mio model con la sua scena JME. La classe principale del gioco chiama `motore.aggiorna(aereo, tpf)` ad ogni frame e poi legge posizione e angoli per aggiornare il nodo 3D. Ho aggiunto i getter che mancavano:

```java
public double getVelocita()    { return velocita; }
public double getQuota()       { return quota; }
public double getAngoloPitch() { return angoloPitch; }
public double getAngoloBanco() { return angoloBanco; }
public double getThrottle()    { return throttle; }
public double getCarburante()  { return carburante; }
public boolean isMotoreAcceso(){ return motoreAcceso; }
```

Con questi metodi Bruno accede allo stato dell'aereo senza toccare gli attributi privati — principio di **incapsulamento**: i dati interni di una classe non sono accessibili direttamente dall'esterno ma solo tramite metodi controllati.

Nel codice finale del progetto ho visto che la stessa cosa viene fatta nella classe `AirplaneModel` con i metodi di telemetria:

```java
public double getAltitude()      { return physicsPosition.y - groundLevel; }
public double getSpeed()         { return velocity.getMagnitude(); }
public double getThrottle()      { return throttle; }
public double getVerticalClimb() { return velocity.y; }
public EulerAngle getOrientation() { return physicsRotation; }
```

Questi vengono letti dal `GameController` ogni tick e passati al `GameView` tramite `updateTelemetry()`.

### 31 marzo 2026

## Classe TorreControllo e sistema di messaggi al giocatore

Appunti:

Ho implementato la `TorreControllo` che, in base allo stato del velivolo, invia avvisi al giocatore. I messaggi vengono poi visualizzati sull'HUD da Bruno:

```java
public class TorreControllo {
    private List<String> messaggi = new ArrayList<>();

    public void aggiorna(Aereo aereo) {
        messaggi.clear();
        if (aereo.getQuota() < 100 && aereo.getVelocita() > 50) {
            messaggi.add("Torre: Attenzione, quota troppo bassa!");
        }
        if (aereo.getCarburante() < aereo.getCarburanteMax() * 0.1) {
            messaggi.add("Torre: Carburante in riserva, rientro immediato.");
        }
        if (!aereo.isMotoreAcceso()) {
            messaggi.add("Torre: Perdita di motore rilevata. Preparare emergenza.");
        }
    }

    public List<String> getMessaggi() { return messaggi; }
}
```

I messaggi sono quelli che avevo scritto su carta nelle prime sessioni, ora tradotti in codice.

### 1 aprile 2026

## IAS vs TAS: velocità indicata e velocità vera

Appunti:

In aviazione si distingue tra **IAS** (Indicated Air Speed, velocità indicata dallo strumento) e **TAS** (True Air Speed, velocità effettiva rispetto all'aria). La relazione è: **TAS = IAS · √(ρ₀/ρ)** quindi a quote più alte la stessa TAS corrisponde a una IAS più bassa. L'HUD mostrerà l'IAS, come in un aereo reale:

```java
public double getIAS() {
    double rho = AtmosferaISA.getDensita(quota);
    // l'aereo vola alla TAS, l'indicatore mostra l'IAS
    return velocita * Math.sqrt(rho / Costanti.RHO0);
}
```

Ho anche aggiunto il calcolo del **Mach number**: rapporto tra la velocità del velivolo e la velocità del suono nell'aria alla quota attuale.

```java
public double getMach() {
    // velocità del suono varia con la temperatura
    double temp = 288.15 - 0.0065 * quota; // K
    double velSuono = Math.sqrt(1.4 * 287.05 * temp);
    return velocita / velSuono;
}
```

### 2 aprile 2026

## Correzione dei coefficienti aerodinamici dopo i primi test visivi

Appunti:

Oggi abbiamo fatto i primi test veri con il model collegato alla grafica. Ho dovuto correggere diversi valori perché il comportamento non era realistico:

- CD0 troppo basso → l'aereo accelerava senza fine → portato da 0.02 a 0.035
- Massa troppo bassa per la spinta → rapporto spinta/peso eccessivo → massa aumentata a 7500 kg
- Consumo carburante esagerato → esaurito in 30 secondi → ridotto consumoMax

```java
public Aereo() {
    this.massa           = 7500;   // kg
    this.superficieAlare = 30.0;   // m²
    this.spintaMax       = 80000;  // N
    this.consumoMax      = 0.8;    // kg/s a piena potenza
    this.carburanteMax   = 3000;   // kg
    this.carburante      = 3000;
}
```

Con questi valori la velocità di stallo risulta circa 62 m/s, che è sensata per un velivolo di quella massa.

Nella versione finale del progetto la stessa logica di tuning è presente in `AirplaneModel`: i valori come `maxEnginePower = 20000`, `liftCoefficient = 1.5`, `dragCoefficient = 0.2` sono stati calibrati empiricamente nei test.

### 3 aprile 2026

## Protezione anti-stallo automatica e limiti di manovra

Appunti:

Ho aggiunto una protezione base che impedisce al pitch di aumentare ulteriormente quando la velocità si avvicina pericolosamente allo stallo, simulando i sistemi di protezione presenti nei velivoli moderni:

```java
private void protezioneStalloAutomatica(double rho) {
    double vs = calcolaVelocitaStallo(rho);
    // se la velocità è entro il 10% dello stallo e pitch sta salendo
    if (velocita < vs * 1.1 && angoloPitch > 0) {
        // blocca l'aumento del pitch
        angoloPitch = Math.min(angoloPitch, Math.toRadians(5.0));
        System.out.println("ATTENZIONE: prossimo allo stallo");
    }
}
```

Ho anche aggiunto dei limiti di manovra sul rateo di variazione del pitch: non può cambiare di più di 2°/s per evitare transizioni troppo brusche che sembrano innaturali visivamente.

Nel progetto finale il controllo della sensibilità dei comandi è gestito tramite il metodo `controlScale()` in `AirplaneModel`, che scala i torque applicati in proporzione alla velocità:

```java
private double controlScale() {
    return Math.min(1, forwardSpeed / 20);
}
```

Questo significa che a velocità basse (sotto 20 m/s) i comandi sono smorzati, esattamente come in un aereo reale dove il velivolo è meno manovrabile a bassa velocità.

### 4 aprile 2026

## Studio del GameController e del pattern MVC nel progetto

Appunti:

Ho studiato con attenzione il `GameController.java` per capire come funziona il game loop del progetto. Il controller usa un `javax.swing.Timer` con intervallo di 30 ms per il tick della fisica:

```java
physicsTimer = new Timer(30, e -> physicsTick());
```

Il metodo `physicsTick()` fa tre cose ad ogni chiamata: aggiorna la fisica chiamando `airplane.tick()`, aggiorna la posizione della camera, e aggiorna i valori dell'HUD:

```java
private void physicsTick() {
    airplane.tick();
    if (cameraController != null) cameraController.updatePosition();
    view.updateTelemetry(
        airplane.getThrottle(), airplane.getSpeed(),
        airplane.getAltitude(), airplane.getVerticalClimb(),
        airplane.getOrientation().y, airplane.getOrientation().z);
    view.repaint();
}
```

Il **`javax.swing.Timer`** è un timer Swing che esegue il suo `ActionListener` nel thread EDT (Event Dispatch Thread), garantendo che le operazioni di rendering e fisica siano thread-safe rispetto all'interfaccia grafica. Ho trovato utile la documentazione ufficiale Oracle su questo tema:
https://docs.oracle.com/javase/tutorial/uiswing/misc/timer.html

### 5 aprile 2026

## Analisi del sistema di pausa e focus listener

Appunti:

Ho studiato come il `GameController` gestisce la pausa. Il gioco si mette in pausa automaticamente quando la finestra perde il focus (`focusLost`) e riprende quando lo riprende (`focusGained`). Questo è implementato tramite `FocusListener`:

```java
@Override
public void focusLost(FocusEvent e) {
    pause();
    view.repaint();
}

@Override
public void focusGained(FocusEvent e) {
    unpause();
    applySettings();
    view.repaint();
}
```

Quando si mette in pausa, il `physicsTimer` viene fermato (`physicsTimer.stop()`) e `airplane.stopPhysics()` disabilita il calcolo della fisica nel metodo `tick()`. Questo garantisce che i valori dell'aereo restino congelati durante la pausa senza consumare CPU inutilmente.

Ho anche capito come funziona il reset: `airplane.resetPhysics()` azzera posizione, velocità e orientamento riportando tutto alla condizione iniziale, e poi traduce la mesh del modello 3D alla posizione di origine. Questo è necessario perché la mesh viene modificata direttamente dalle trasformazioni (non è immutabile).

### 6 aprile 2026

## Sabato: revisione generale, Javadoc e pulizia del codice

Appunti:

Ho dedicato il sabato a rivedere tutto il codice: aggiunto Javadoc ai metodi principali, rinominato alcune variabili ambigue, rimosso codice duplicato. Esempio di Javadoc aggiunto a `calcolaPortanza`:

```java
/**
 * Calcola la forza di portanza aerodinamica.
 * Formula: L = 0.5 * rho * v^2 * S * CL
 *
 * @param rho densità dell'aria in kg/m³
 * @param vel velocità del velivolo in m/s
 * @param cl  coefficiente di portanza (adimensionale)
 * @return portanza in Newton
 */
private double calcolaPortanza(double rho, double vel, double cl) {
    return 0.5 * rho * vel * vel * superficieAlare * cl;
}
```

Il **Javadoc** è un sistema di documentazione integrato in Java: i commenti con `/** ... */` vengono trasformati automaticamente in documentazione HTML leggibile.

Ho inoltre analizzato come il `SimplexNoise` viene usato nella classe `Terrain` per generare il terreno procedurale. Il **Simplex Noise** è un algoritmo di generazione di rumore pseudo-casuale sviluppato da Ken Perlin nel 2001, più efficiente del Perlin Noise classico. Nel progetto viene usato così:

```java
verts[x][z] = new Vector3(
    (x - gridWidth/2.0) * gridInterval,
    Math.max(height + Math.pow(SimplexNoise.noise(x*frequency, z*frequency)*amplitude, 3), height+waterLevel),
    (z - gridLength/2.0) * gridInterval + 300000);
```

Il cubo della funzione noise amplifica le variazioni: valori vicini a 0 restano bassi (mare piatto), valori alti diventano molto alti (montagne). Il parametro `frequency` controlla la scala del terreno (bassa = colline dolci, alta = terreno frastagliato).

Ho usato Claude per farmi spiegare la matematica del Simplex Noise e per confrontare le sue proprietà rispetto al Perlin Noise classico — utile per capire perché è più adatto a questo tipo di applicazione.

### 7 aprile 2026

## Test finale integrato e aggiunta dell'UML del model

Appunti:

Ultima sessione di test insieme a Bruno. Ho verificato che tutto funzionasse correttamente:

1. Velocità di stallo calcolata ≈ 62 m/s con i parametri attuali → sensata
2. La protezione anti-stallo funzionava senza essere troppo invasiva
3. Il consumo carburante con throttle medio (0.5) dava circa 25 minuti di autonomia → accettabile per il gioco

Ultima correzione: quando l'aereo tocca il suolo (quota = 0 con velocità > 5 m/s) ora viene segnalato un "crash" con un flag booleano che Gioele userà per mostrare la schermata di game over:

```java
public boolean isCrashed() {
    return quota <= 0 && velocita > 5.0;
}
```

Nel progetto finale la stessa logica è gestita in `AirplaneModel` dentro `updatePosition()`: quando la posizione Y scende sotto `groundLevel`, la velocità verticale viene azzerata e la posizione Y viene bloccata. Nel `GameController` si potrà in futuro aggiungere il controllo sulla velocità all'impatto per distinguere un atterraggio morbido da uno schianto.
Ora abbiamo anche finito di creare l'UML per il model, mancherebbe ancora l'UML del controller e del view ma i tempi sono ristretti.

<img src="https://github.com/user-attachments/assets/af3020e7-712a-46f3-85fb-4279e327263d" alt="UML" width="1500">
