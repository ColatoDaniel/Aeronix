# Aeronix
**Aeronix è un videogioco che simula fisiche realistiche di un velivolo, un gioco dedicato a tutti quelli che sono interessati nell'addentrarsi o semplicemente scoprire nuove cose.**

Aeronix sarà disponibile per PC Desktop.

*IDE: NetBeans* <br>
*JDK: 19*


### 28/02/2026
Ci ho messo un po' di tempo ma ho capito che semplicemente dovevo fare un downgrade della versione della piattaforma di Java dalla 25esima alla 19esima, dato che l'esecuzione dell'applicazione test falliva in quanto la versione di Java era troppo nuova rispetto a quella di Gradle.

### 02/03/2026
Ho provato ad importare un modello da internet e impostare una luce così da poterlo illuminare, ma non riesco a vederlo.

### 03/03/2026
Sono riuscito a sistemare, rileggendo il codice ho notato che la scala dell'oggetto dell'auto era veramente troppo piccola, quindi l'ho moltiplicata per 1 milione e adesso è decisamente più grande.

### 07/03/2026
Ho aggiunto un altro paio di modelli .glb all'interno del codice, JME li renderizza bene. Sto prendendo dimestichezza con la creazione/importazione di modelli e l'impostazione della luce e texture.

### 11/03/2026
Ho iniziato a studiare come gestire la camera in JMonkeyEngine. La camera di default funziona in modalità "free cam", non adatta a un simulatore di volo. Ho cercato su internet e sul forum ufficiale di JME come implementare una camera che segua il velivolo:
https://hub.jmonkeyengine.org/t/follow-camera-airplane/

Ho capito che JME offre una classe già pronta chiamata `ChaseCamera`. La **ChaseCamera** è una camera che segue un nodo target mantenendo una distanza e un offset verticale configurabili, ruotando con lui.

### 12/03/2026
Ho implementato la `ChaseCamera` agganciata al nodo del velivolo. Ho seguito questo tutorial su YouTube:
*"JMonkeyEngine 3 - Follow Camera Tutorial"* - canale **jMonkeyEngine**

```java
ChaseCamera chaseCam = new ChaseCamera(cam, aeroNode, inputManager);
chaseCam.setDefaultDistance(15f);
chaseCam.setDefaultVerticalRotation(0.3f);
chaseCam.setMinVerticalRotation(-0.5f);
chaseCam.setMaxVerticalRotation(1.0f);
chaseCam.setSmoothMotion(true); // movimento fluido senza scatti
```

`setSmoothMotion(true)` è importante: senza di essa la camera seguiva il nodo con scatti brutti ad ogni frame. Con l'interpolazione abilitata il movimento è molto più naturale.

### 13/03/2026
Ho creato un nodo principale `aeroNode` a cui sono agganciati il modello 3D e la camera. Il concetto chiave è lo **scene graph**: struttura ad albero dove ogni nodo figlio eredita le trasformazioni del nodo padre (posizione, rotazione, scala).

```java
Node aeroNode = new Node("aereo");
aeroNode.attachChild(modello3D);
rootNode.attachChild(aeroNode);
```

Così quando sposto o ruoto `aeroNode`, il modello si muove con lui automaticamente. Non devo aggiornare ogni oggetto separatamente.

### 16/03/2026
Ho lavorato sulla gestione della luce. In JME esistono diversi tipi:
- **DirectionalLight**: simula il sole, raggi paralleli con direzione fissa
- **AmbientLight**: luce uniforme senza direzione, evita che le zone in ombra siano completamente nere
- **PointLight**: sorgente puntiforme che irradia in tutte le direzioni

```java
DirectionalLight sole = new DirectionalLight();
sole.setColor(ColorRGBA.White.mult(1.3f));
sole.setDirection(new Vector3f(-0.5f, -1f, -0.5f).normalizeLocal());
rootNode.addLight(sole);

AmbientLight ambiente = new AmbientLight();
ambiente.setColor(ColorRGBA.White.mult(0.3f));
rootNode.addLight(ambiente);
```

Il `mult(1.3f)` sulla luce diretta la rende leggermente più intensa del bianco puro, dando più contrasto alla scena.

### 17/03/2026
Ho trovato su Sketchfab un modello .glb di aereo da caccia stilizzato con licenza libera (https://sketchfab.com). L'ho scaricato e importato nel progetto. In JME i modelli .glb si caricano con l'asset manager:

```java
Spatial modello3D = assetManager.loadModel("Models/aereo/aereo.glb");
modello3D.scale(0.05f); // scala corretta questa volta
aeroNode.attachChild(modello3D);
```

Stavolta ho verificato subito la scala prima di andare avanti, dopo l'esperienza del modello invisibile della settimana scorsa.

### 18/03/2026
Ho iniziato a lavorare sul **terrain** (terreno). JME ha un sistema di HeightMap: genera terreni 3D a partire da un'immagine in scala di grigi dove le zone più chiare corrispondono a quote più alte.

```java
AbstractHeightMap heightmap = new ImageBasedHeightMap(
    assetManager.loadTexture("Textures/terrain/heightmap.png").getImage(), 1f);
heightmap.load();

TerrainQuad terrain = new TerrainQuad("terrain", 65, 513, heightmap.getHeightMap());
terrain.setLocalTranslation(0, -50, 0);
terrain.setLocalScale(2f, 0.5f, 2f);
rootNode.attachChild(terrain);
```

Ho scaricato una heightmap gratuita da https://tangrams.github.io/heightmapper/. Per ora il terreno è abbastanza pianeggiante, lo renderò più vario in seguito.

### 19/03/2026
Ho applicato una texture al terreno per renderlo visivamente credibile invece del grigio uniforme di default. In JME si usa un materiale `TerrainLighting.j3md`:

```java
Material matTerrain = new Material(assetManager, "Common/MatDefs/Terrain/TerrainLighting.j3md");
matTerrain.setTexture("DiffuseMap", assetManager.loadTexture("Textures/terrain/grass.jpg"));
matTerrain.setFloat("DiffuseMap_0_scale", 64f); // ripete la texture 64 volte
terrain.setMaterial(matTerrain);
```

Il parametro `DiffuseMap_0_scale` controlla quante volte la texture si ripete sulla superficie del terreno. Con valori troppo bassi la texture appare gigante e pixelata, troppo alti e non si distingue nulla.

### 20/03/2026
Ho aggiunto una **skybox**: un cubo enorme con texture del cielo all'interno che dà l'impressione di essere immersi in un ambiente realistico. In JME:

```java
Spatial sky = SkyFactory.createSky(assetManager,
    assetManager.loadTexture("Textures/sky/right.jpg"),
    assetManager.loadTexture("Textures/sky/left.jpg"),
    assetManager.loadTexture("Textures/sky/top.jpg"),
    assetManager.loadTexture("Textures/sky/bottom.jpg"),
    assetManager.loadTexture("Textures/sky/front.jpg"),
    assetManager.loadTexture("Textures/sky/back.jpg"),
    SkyFactory.EnvMapType.CubeMap);
rootNode.attachChild(sky);
```

Ho trovato un set di texture skybox gratuite con cielo sereno su https://opengameart.org/content/sky-box-sunny-day. La differenza visiva rispetto allo sfondo nero era enorme.

### 23/03/2026
Ho iniziato a lavorare sull'**HUD** (Heads-Up Display): l'interfaccia 2D sovrapposta alla scena 3D che mostra le informazioni di volo. Ho usato `BitmapText` di JME per creare testi a schermo in modo semplice:

```java
BitmapFont font = assetManager.loadFont("Interface/Fonts/Default.fnt");

BitmapText txtVelocita = new BitmapText(font, false);
txtVelocita.setSize(font.getCharSet().getRenderedSize());
txtVelocita.setColor(ColorRGBA.Green);
txtVelocita.setLocalTranslation(10, cam.getHeight() - 10, 0);
guiNode.attachChild(txtVelocita);
```

I testi HUD si aggiungono al `guiNode` invece che al `rootNode`: in questo modo rimangono fissi sullo schermo e non si muovono con la camera.

### 24/03/2026
Ho aggiunto tutti gli indicatori dell'HUD: velocità, quota, throttle e carburante. Li aggiorno nel metodo `simpleUpdate` ad ogni frame leggendo i dati dal model di Daniel:

```java
@Override
public void simpleUpdate(float tpf) {
    // aggiorno la fisica
    motore.aggiorna(aereo, tpf);

    // aggiorno l'HUD
    txtVelocita.setText(String.format("IAS: %.0f m/s", aereo.getIAS()));
    txtQuota.setText(String.format("ALT: %.0f m", aereo.getQuota()));
    txtThrottle.setText(String.format("THR: %.0f%%", aereo.getThrottle() * 100));
    txtCarburante.setText(String.format("FUEL: %.0f kg", aereo.getCarburante()));
}
```

`simpleUpdate` è il metodo principale del game loop di JME: viene chiamato automaticamente ad ogni frame. `tpf` (time per frame) è il tempo trascorso dall'ultimo frame in secondi, utile per rendere la simulazione indipendente dalla velocità del PC.

### 25/03/2026
Ho collegato la rotazione del nodo 3D agli angoli calcolati dal model fisico di Daniel. Ho dovuto fare attenzione agli assi di riferimento di JME che sono diversi dalla convenzione che usavamo sulla carta:

```java
// In JME: X = destra, Y = su, Z = verso di noi
Quaternion rotazione = new Quaternion();
rotazione.fromAngles(
    -aereo.getAngoloPitch(),  // pitch: negativo perché JME usa y-up
     aereo.getAngoloBanco(),  // bank
     0f                       // yaw (per ora non gestito)
);
aeroNode.setLocalRotation(rotazione);
```

**Quaternion**: rappresentazione matematica di una rotazione 3D che evita il problema del "gimbal lock" (blocco cardanico) che si avrebbe usando tre angoli separati (Euler angles). JME usa i quaternion internamente per tutte le rotazioni.

### 26/03/2026
Ho aggiunto la posizione del velivolo nella scena basandomi sulla quota e sulla velocità orizzontale calcolate da Daniel:

```java
float x = aeroNode.getLocalTranslation().x + (float)(aereo.getVelocita() * Math.cos(aereo.getAngoloPitch()) * tpf);
float y = (float) aereo.getQuota();
float z = aeroNode.getLocalTranslation().z;

aeroNode.setLocalTranslation(x, y, z);
```

Per ora il velivolo si muove solo sull'asse X (avanti) e Y (quota). La gestione del movimento nelle tre dimensioni con virata sarà il prossimo step.

### 27/03/2026
Ho aggiunto la visualizzazione dei messaggi della `TorreControllo` di Daniel sull'HUD. I messaggi appaiono in rosso in basso a sinistra dello schermo e scompaiono dopo 3 secondi:

```java
private float tempoMessaggio = 0f;

// in simpleUpdate:
List<String> msgs = torreControllo.getMessaggi();
if (!msgs.isEmpty()) {
    txtTorre.setText(msgs.get(0));
    txtTorre.setColor(ColorRGBA.Red);
    tempoMessaggio = 3.0f;
}
if (tempoMessaggio > 0) {
    tempoMessaggio -= tpf;
    if (tempoMessaggio <= 0) txtTorre.setText("");
}
```

### 30/03/2026
Ho lavorato sulla gestione del movimento nelle tre dimensioni. Quando il velivolo è in virata (bank angle ≠ 0) deve anche cambiare direzione di movimento. Ho calcolato il vettore direzione del moto a partire dagli angoli:

```java
float vx = (float)(aereo.getVelocita() * Math.cos(aereo.getAngoloPitch()) * Math.cos(angoloDirezione));
float vy = (float)(aereo.getVelocita() * Math.sin(aereo.getAngoloPitch()));
float vz = (float)(aereo.getVelocita() * Math.cos(aereo.getAngoloPitch()) * Math.sin(angoloDirezione));

Vector3f pos = aeroNode.getLocalTranslation();
aeroNode.setLocalTranslation(pos.x + vx * tpf, pos.y + vy * tpf, pos.z + vz * tpf);
```

`angoloDirezione` viene aggiornato in base alla forza centripeta calcolata dal model di Daniel.

### 31/03/2026
Ho aggiunto il rilevamento della collisione con il terreno. In JME si può usare il sistema di **collision detection** per verificare se il nodo dell'aereo interseca il terreno:

```java
CollisionResults results = new CollisionResults();
aeroNode.collideWith(terrain.getWorldBound(), results);

if (results.size() > 0 || aereo.getQuota() <= 0) {
    if (aereo.isCrashed()) {
        mostraGameOver();
    }
}
```

Per ora uso anche il controllo diretto sulla quota (`aereo.getQuota() <= 0`) come fallback, perché la collision detection con il terreno può essere imprecisa a velocità elevate.

### 01/04/2026
Ho implementato la schermata di **game over** che appare quando il velivolo si schianta. Uso un pannello semitrasparente con testo sovrapposto alla scena:

```java
private void mostraGameOver() {
    // pannello scuro semitrasparente
    Picture pannello = new Picture("gameOver");
    pannello.setImage(assetManager, "Textures/ui/black.png", true);
    pannello.setWidth(cam.getWidth());
    pannello.setHeight(cam.getHeight());
    pannello.setLocalTranslation(0, 0, -1);
    guiNode.attachChild(pannello);

    BitmapText testo = new BitmapText(font, false);
    testo.setText("GAME OVER");
    testo.setSize(60);
    testo.setColor(ColorRGBA.Red);
    testo.setLocalTranslation(cam.getWidth()/2 - 150, cam.getHeight()/2, 0);
    guiNode.attachChild(testo);
}
```

### 02/04/2026
Ho migliorato il terreno aggiungendo una seconda texture per le zone rocciose in quota. JME permette di miscelare più texture sul terreno usando una **splat map**: un'immagine in cui i canali RGBA indicano quanto di ogni texture usare in ogni punto.

```java
matTerrain.setTexture("DiffuseMap_1", assetManager.loadTexture("Textures/terrain/rock.jpg"));
matTerrain.setFloat("DiffuseMap_1_scale", 32f);
matTerrain.setTexture("AlphaMap", assetManager.loadTexture("Textures/terrain/splat.png"));
```

Dove la splat map è rossa appare l'erba, dove è verde appare la roccia. Il risultato visivo è molto più credibile di una singola texture uniforme.

### 03/04/2026
Ho aggiunto un effetto di **fog** (nebbia) per dare profondità alla scena e nascondere il pop-in del terreno alle lunghe distanze. In JME la nebbia si imposta come filtro post-processing:

```java
FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
FogFilter fog = new FogFilter();
fog.setFogColor(new ColorRGBA(0.7f, 0.8f, 1.0f, 1.0f)); // azzurro cielo
fog.setFogDistance(800f);
fog.setFogDensity(1.5f);
fpp.addFilter(fog);
viewPort.addProcessor(fpp);
```

Il **post-processing** è un insieme di effetti applicati all'immagine già renderizzata, prima di mostrarla a schermo. La nebbia blu-azzurra dà anche una sensazione di atmosfera realistica simile all'haziness che si vede in quota.

Guardato questo video per capire meglio il sistema di post-processing:
*"JMonkeyEngine Post Processing Effects"* - canale **MakeGamesWithUs** su YouTube

### 04/04/2026
Ho iniziato a studiare il progetto di Daniel più nel dettaglio per capire come il rendering e la fisica si devono interfacciare. Ho visto che il progetto segue il pattern **MVC (Model-View-Controller)**: il model (`AirplaneModel`) contiene solo i dati fisici, il view (`GameView`, `RenderingPanel`) si occupa solo del disegno, e il controller (`GameController`) fa da collante tra i due.

Questo mi ha fatto capire che il `RenderingPanel` non deve mai toccare direttamente i dati fisici: legge solo quello che il `GameController` gli passa tramite `updateTelemetry()`. Ho riorganizzato di conseguenza il mio codice per rispettare questa separazione. Ho trovato un video molto chiaro su YouTube sul pattern MVC applicato ai giochi:
*"MVC Pattern in Java Game Development"* - canale **Coding with John**

### 05/04/2026
Ho guardato con attenzione il `RenderingPanel.java` per capire come funziona il renderer 3D software che abbiamo scritto. Tutto il rendering avviene su una `BufferedImage` che viene poi disegnata sul pannello con `g.drawImage`. Il metodo `paintTriangle` riempie i triangoli pixel per pixel usando righe orizzontali (**scanline rendering**):

```java
private void paintTriangle(Point p1, Point p2, Point p3, Color color) {
    int rgb = toIntRGB(color);
    // ordinamento vertici per Y crescente (bubble sort a 3 elementi)
    Point tmp;
    if (p1.y > p2.y) { tmp=p1; p1=p2; p2=tmp; }
    if (p2.y > p3.y) { tmp=p2; p2=p3; p3=tmp; }
    if (p1.y > p2.y) { tmp=p1; p1=p2; p2=tmp; }
    // ...
}
```

**Scanline rendering**: tecnica di rasterizzazione che disegna un triangolo riga per riga (scanline). Per ogni riga Y si calcolano le intersezioni con i lati del triangolo e si riempie il segmento orizzontale compreso. È uno degli algoritmi più veloci per la rasterizzazione software.

Il metodo `hLine` poi scrive i pixel direttamente nella `BufferedImage` tramite `setDataElements`, che è molto più veloce del classico `g.setColor + g.fillRect` chiamato pixel per pixel.

### 06/04/2026
Sabato di lavoro per sistemare diversi problemi emersi nei test:

Il modello 3D ruotava nel verso sbagliato sul pitch → invertito il segno dell'angolo nella conversione al quaternion.

Il terreno mostrava **z-fighting** a distanza: uno sfarfallio causato da due superfici geometricamente troppo vicine che si "contendono" i pixel. Ho regolato i parametri della camera:

```java
cam.setFrustumFar(5000f);  // distanza massima di rendering
cam.setFrustumNear(0.5f);  // distanza minima (più alta = meno z-fighting)
```

Ho anche attivato il **LOD** (Level of Detail) del terreno che riduce automaticamente il numero di poligoni nelle zone lontane dalla camera, migliorando molto le performance:

```java
List<Camera> cameras = new ArrayList<>();
cameras.add(getCamera());
TerrainLodControl lodControl = new TerrainLodControl(terrain, cameras);
terrain.addControl(lodControl);
```

Ho anche rivisto come funziona il fog nel `RenderingPanel`. Nel codice la nebbia viene applicata per interpolazione lineare tra il colore del triangolo e il colore della nebbia, in base alla distanza dalla camera:

```java
private Color resolveColor(Triangle tri, double dist) {
    Color lit = tri.getColorWithLighting();
    if (fogEnabled && dist > fogStart) {
        if (dist >= fogFull) return fogColor;
        double t = (dist - fogStart) / (fogFull - fogStart);
        int r = clamp(lit.getRed()   + (int)((fogColor.getRed()   - lit.getRed())   * t * t));
        int g = clamp(lit.getGreen() + (int)((fogColor.getGreen() - lit.getGreen()) * t * t));
        int b = clamp(lit.getBlue()  + (int)((fogColor.getBlue()  - lit.getBlue())  * t * t));
        return new Color(r, g, b);
    }
    return lit;
}
```

Notare che il parametro `t` viene elevato al quadrato (`t * t`) invece di usarlo lineare: questo dà un effetto di nebbia che aumenta lentamente all'inizio e accelera verso la distanza massima, risultando più naturale.

Il fog viene inizializzato nel `GameController` con questi parametri:

```java
rp.setFog(camera.getFarClipDistance() * 0.6, camera.getFarClipDistance(), new Color(91, 215, 252));
```

Il colore `(91, 215, 252)` è lo stesso dello sfondo (`backgroundColor` nel `RenderingPanel`), così i triangoli lontani sfumano perfettamente nello sfondo senza un taglio netto.

### 07/04/2026
Ultima sessione di test insieme a Daniel. Abbiamo verificato tutto end-to-end: fisica, grafica, HUD, messaggi torre, game over. Il gioco funziona in modo abbastanza fluido.

Ho aggiunto un piccolo indicatore visivo dell'angolo di bank sull'HUD: una linea orizzontale che si inclina con il velivolo, come l'orizzonte artificiale dei velivoli reali:

```java
// linea orizzonte artificiale (semplificata)
txtBanco.setText(String.format("BANK: %.1f°", Math.toDegrees(aereo.getAngoloBanco())));
```

Ho anche capito meglio come funziona il `SidePanel` dentro il `GameView`: il pannello laterale disegna i quadranti strumenti usando l'immagine `AirplaneDials.png` e poi sovrappone gli aghi tramite metodi come `drawDialNeedle` e `drawTurnCoordinator`, che disegnano linee ruotate usando `Math.cos` e `Math.sin`. Ogni quadrante ha il suo centro e la sua lunghezza dell'ago, e la rotazione dipende dai valori di telemetria passati da `GameController.physicsTick()`.

La prossima fase sarà aggiungere suoni (motore, vento, allarmi) e migliorare i modelli 3D.
