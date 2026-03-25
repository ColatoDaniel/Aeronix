# ✈ AERONIX Flight Simulator
**v0.1.0 Alpha — JDK 21 — Windows Desktop — Cirrus SR22**

---

## Architettura MVC

```
com.aeronix/
├── Main.java                        ← Entry point
│
├── model/                           ← MODEL (dati puri, nessuna UI)
│   ├── AircraftModel.java           ← Stato completo SR22 + getter/setter
│   ├── Airport.java                 ← Record aeroporto (ICAO, coord, pista...)
│   ├── AirportDatabase.java         ← ~55 aeroporti italiani
│   ├── AtcScript.java               ← Dialogo completo Phoenix Radio
│   └── Settings.java                ← Persistenza via Java Preferences API
│
├── controller/                      ← CONTROLLER (logica di gioco)
│   ├── FlightController.java        ← Physics tick 60Hz, ATC trigger, listeners
│   └── InputController.java         ← Tutti i tasti + mouse → FlightController
│
├── view/                            ← VIEW (solo rendering, nessuna logica)
│   ├── SplashView.java              ← Schermata caricamento (stile X-Plane 12)
│   ├── AirportSelectionView.java    ← Selezione aeroporto italiano
│   ├── FictionalLoadingView.java    ← Caricamento fittizio + globo wireframe
│   ├── FlightView.java              ← Volo: scena + HUD + motore + ATC radio
│   └── OptionsView.java             ← Opzioni: grafica, audio, tasti, sim
│
└── audio/
    └── AudioManager.java            ← Engine loop, vento, click ATC, avvisi
```

---

## Flow delle schermate

```
Main
 └→ SplashView          (loading simulato ~8 sec)
      └→ AirportSelectionView  (scegli aeroporto italiano)
           └→ FictionalLoadingView  (caricamento fittizio con globo 3D)
                └→ FlightView       (simulazione di volo)
```

---

## Dialogo ATC — Phoenix Radio (automatico)

Il sistema ATC si attiva **automaticamente** in base a:
- **Fase aereo** (`PARKED → TAXIING → LINEUP → TAKEOFF → CLIMB...`)
- **Quota** (500 ft → call in volo, 1200 ft → richiesta cambio frequenza)

### Sequenza completa:
| # | Chi | Messaggio |
|---|-----|-----------|
| 1 | I-CDBG (pilota) | Phoenix radio, I-CDBG, Buongiorno |
| 2 | Phoenix Radio | I-CDBG, Phoenix radio, buongiorno. QNH 1012, avanti |
| 3 | I-CDBG | SR22, QNH 1012, VFR, senza piano di volo, Phoenix–Phoenix... chiede istruzioni rullaggio |
| 4 | Phoenix Radio | I-BG, rullare al punto attesa pista 26L via Mike November Oscar... |
| 5 | I-BG | Readback rullaggio |
| 6 | I-BG | Al punto attesa pista 26L, pronto alla partenza |
| 7 | Phoenix Radio | Finale pista libero, allineamento e decollo a discrezione pista 26L |
| 8 | I-BG | Readback clearance decollo |
| 9 | Phoenix Radio | I-BG, in volo agli 12' *(attivato a ~500 ft)* |
| 10 | I-BG | Phoenix radio, pronti per il cambio *(a ~1200 ft)* |
| 11 | Phoenix Radio | Contattare informazioni sulla 125.243, buona giornata |

---

## Tasti principali

| Azione | Tasto |
|--------|-------|
| Pitch su/giù | ↑↓ / W S |
| Rollio dx/sx | ←→ / A D |
| Ruddder | Q / E |
| Manetta +/- | Page Up / Page Down |
| Carrello | G |
| Flap su/giù | F / Shift+F |
| Autopilota | Z |
| Pausa | P / Esc |
| Vista cabina/esterna | 1 / 2 |
| Avvia/ferma motore | Ctrl+E |

*(tutti i comandi elencati in OptionsView → tab "Tastiera & Mouse")*

---

## Compilazione

**Prerequisiti:** JDK 21, Maven 3.9+

```bash
# Clona / apri il progetto
cd Aeronix

# Compila e crea fat JAR
mvn package

# Avvia direttamente
java --enable-preview -jar target/aeronix-simulator-0.1.0-ALPHA-fat.jar

# Oppure usa l'exe Windows generato da launch4j
target/Aeronix.exe
```

---

## File audio attesi (src/main/resources/audio/)
```
engine_loop.wav     ← sintetizzato se assente
wind_loop.wav       ← sintetizzato se assente
atc_click.wav       ← sintetizzato se assente
stall_warning.wav   ← sintetizzato se assente
gear_down.wav       ← sintetizzato se assente
```
*(AudioManager genera tutti i suoni sinteticamente se i file non esistono)*

---

## To-do / Roadmap
- [ ] Integrare modello Blender SR22 (OBJ → JOGL/LWJGL)
- [ ] Renderer OpenGL via JOGL per terreno e cielo
- [ ] G1000 PFD/MFD completo
- [ ] Meteo dinamico (vento, turbolenza)
- [ ] TTS italiano per ATC (Java Speech API / FreeTTS)
- [ ] Mini-mappa nel cockpit
- [ ] Salvataggio situazione di volo
