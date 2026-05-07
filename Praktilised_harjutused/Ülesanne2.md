# Ülesanne 2 — Arhitektuuri diagnoos

## 1. Miks monoliit probleeme tekitab?
Monoliitse arhitektuuri puhul on kogu rakenduse kood (kasutajate haldus, tooted, maksmine) ühes suures andmebaasis ja serveris. Kui allahindluse ajal tekib suur koormus ühes konkreetses moodulis (näiteks kõik üritavad korraga maksta), on kogu serveri ressurss ammendatud. Selle tulemusena aeglustub või peatub terve rakendus.

## 2. Milline osa võiks olla mikroteenus?
**Valik:** Maksete töötlemine (Payment Gateway).

**Miks?** Maksesüsteem on allahindluste ajal kõige koormatum osa. Kui muuta maksmine eraldi mikroteenuseks, saab sellele vajadusel serveri ressurssi juurde anda (skaleerida) iseseisvalt. Lisaks, kui maksesüsteemis on tõrge, saavad kasutajad endiselt rakenduses ringi vaadata ja tooteid ostukorvi lisada.

## 3. Skeem (Maxima mikroteenustena)
Mikroteenustena jagatakse süsteem väiksemateks iseseisvateks mooduliteks.

[Kasutajaliides (Mobiiliäpp / Veeb)]
      |
[API Gateway] -- (suunab päringud õigesse kohta)
      |
  -----------------------------------------
  |                  |                    |
[Kasutajate API]  [Toodete API]      [Maksete API] 
  |                  |                    |
(Andmebaas 1)     (Andmebaas 2)      (Andmebaas 3)
