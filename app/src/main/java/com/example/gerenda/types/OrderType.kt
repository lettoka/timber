package com.example.gerenda.types

enum class OrderType(val title:String,val query:String) {
    NEW("Új megrendelés","""SELECT "rager_r"."rager_0", 
  "rager_r"."ara_szam","rager_r"."vm_kod","rager_r"."ragr_foly", 
  "arajanl_f"."ara_erveny" AS "jo_hatarido","vevok"."vevo_rnev", 
  (CASE 
    WHEN ("arajanl_f"."vevo_kod" < 0) 
    THEN (SELECT "arajanl_v"."vevo_hnev1" FROM "arajanl_v" WHERE ("arajanl_v"."ara_szam" = "arajanl_f"."ara_szam")) 
    ELSE ("vevok"."vevo_hnev1") 
  END) AS "jo_vevo" 
 FROM 
  "rager_r" 
  LEFT OUTER JOIN "arajanl_f" ON "arajanl_f"."ara_szam" = "rager_r"."ara_szam" 
  LEFT OUTER JOIN "vevok" ON "vevok"."vevo_kod" = "arajanl_f"."vevo_kod" 
WHERE 
  ("rager_r"."vm_kod" <> '') AND 
  ("rager_r"."ragr_foly" <> 'I') AND 
  ((SELECT DISTINCT 
    "rager_f"."rager_kom" 
  FROM 
    "rager_f" 
  WHERE 
    ("rager_f"."rager_0" = "rager_r"."rager_0") AND 
    ("rager_f"."rager_kom" = 'I') 
  ) IS NULL) AND 
  ((SELECT DISTINCT 
    "rager_f"."rager_jel_vagva" 
  FROM "rager_f" 
  WHERE 
    ("rager_f"."rager_0" = "rager_r"."rager_0") AND 
    ("rager_f"."rager_jel_vagva" <> 'N') 
    ROWS 1 
  ) IS NULL) AND 
  ((SELECT DISTINCT 
    "rager_0"."rag0_rvagva" 
  FROM "rager_0" INNER JOIN "rager_f" ON "rager_0"."rager_sz"="rager_f"."rager_sz" 
  WHERE 
    ("rager_f"."rager_0" = "rager_r"."rager_0") AND 
    ("rager_0"."rag0_rvagva" <> 'N') 
  ) IS NULL)
  """),
    INPROGRESS("Folyamatban l. megrendelés","""SELECT "rager_r"."rager_0", 
  "rager_r"."ara_szam","rager_r"."vm_kod","rager_r"."ragr_foly", 
  "arajanl_f"."ara_erveny" AS "jo_hatarido","vevok"."vevo_rnev", 
  (CASE 
    WHEN ("arajanl_f"."vevo_kod" < 0) 
    THEN (SELECT "arajanl_v"."vevo_hnev1" FROM "arajanl_v" WHERE ("arajanl_v"."ara_szam" = "arajanl_f"."ara_szam")) 
    ELSE ("vevok"."vevo_hnev1") 
  END) AS "jo_vevo" 
 FROM 
  "rager_r" 
  LEFT OUTER JOIN "arajanl_f" ON "arajanl_f"."ara_szam" = "rager_r"."ara_szam" 
  LEFT OUTER JOIN "vevok" ON "vevok"."vevo_kod" = "arajanl_f"."vevo_kod" 
WHERE 
  ("rager_r"."vm_kod" <> '') AND 
  ("rager_r"."ragr_foly" <> 'I') AND 
  ((SELECT DISTINCT 
    "rager_f"."rager_kom" 
  FROM 
    "rager_f" 
  WHERE 
    ("rager_f"."rager_0" = "rager_r"."rager_0") AND 
    ("rager_f"."rager_kom" = 'I') 
  ) IS NULL) AND 
  (  ((SELECT DISTINCT 
      "rager_f"."rager_jel_vagva" 
    FROM "rager_f" 
    WHERE 
      ("rager_f"."rager_0" = "rager_r"."rager_0") AND 
      ("rager_f"."rager_jel_vagva" <> 'N') 
      ROWS 1 
     ) IS NOT NULL) OR 
    ((SELECT DISTINCT 
     "rager_0"."rag0_rvagva" 
    FROM "rager_0" INNER JOIN "rager_f" ON "rager_0"."rager_sz"="rager_f"."rager_sz" 
     WHERE 
     ("rager_f"."rager_0" = "rager_r"."rager_0") AND 
     ("rager_0"."rag0_rvagva" <> 'N') 
    ) IS NOT NULL)
  ) AND 
  ((SELECT DISTINCT 
      "rager_f"."rager_jel_marad" 
    FROM "rager_f" 
    WHERE 
      ("rager_f"."rager_0" = "rager_r"."rager_0") AND 
      ("rager_f"."rager_jel_marad" = 'N') 
     ) IS NOT NULL)
  """),//("rager_f"."rager_jel_marad" <> 'I') ha a K is ide kell
    CLOSED("Lezárt megrendelés","""SELECT "rager_r"."rager_0", 
  "rager_r"."ara_szam","rager_r"."vm_kod","rager_r"."ragr_foly",
  "arajanl_f"."ara_erveny" AS "jo_hatarido","vevok"."vevo_rnev", 
  (CASE 
    WHEN ("arajanl_f"."vevo_kod" < 0) 
    THEN (SELECT "arajanl_v"."vevo_hnev1" FROM "arajanl_v" WHERE ("arajanl_v"."ara_szam" = "arajanl_f"."ara_szam")) 
    ELSE ("vevok"."vevo_hnev1") 
  END) AS "jo_vevo",
   (CASE 
    WHEN ((SELECT DISTINCT 
    "rager_f"."rager_jel_marad" 
  FROM "rager_f" 
  WHERE 
    ("rager_f"."rager_0" = "rager_r"."rager_0") AND 
    ("rager_f"."rager_jel_marad" <> 'I')
  ) IS NULL) 
    THEN ('I') 
    ELSE ('N') 
  END) AS "feldolgozva" 
 FROM 
  "rager_r" 
  LEFT OUTER JOIN "arajanl_f" ON "arajanl_f"."ara_szam" = "rager_r"."ara_szam" 
  LEFT OUTER JOIN "vevok" ON "vevok"."vevo_kod" = "arajanl_f"."vevo_kod" 
WHERE 
  ("rager_r"."vm_kod" <> '') AND 
  ("rager_r"."ragr_foly" <> 'I') AND 
  ((SELECT DISTINCT 
    "rager_f"."rager_kom" 
  FROM 
    "rager_f" 
  WHERE 
    ("rager_f"."rager_0" = "rager_r"."rager_0") AND 
    ("rager_f"."rager_kom" = 'I') 
  ) IS NULL) AND 
  ((SELECT DISTINCT 
    "rager_f"."rager_jel_marad" 
  FROM "rager_f" 
  WHERE 
    ("rager_f"."rager_0" = "rager_r"."rager_0") AND 
    (("rager_f"."rager_jel_marad" = 'N') OR ("rager_f"."rager_jel_marad" = 'J') OR ("rager_f"."rager_jel_vagva" = 'J'))
  ) IS NULL) 
  """)
}