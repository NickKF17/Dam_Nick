# Valida las consultas SQL de los ejercicios JDBC contra SQLite con datos de ejemplo.
# Extrae las constantes "static final String SQL_xxx = ...;" tal cual de los .java.
import re, sqlite3, sys, os
BASE = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))

def sqls(fichero):
    src = open(fichero, encoding='utf-8').read()
    consts = {}
    for m in re.finditer(r'static final String (\w+) = ((?:"(?:[^"\\]|\\.)*"|[\w\s+])*?);', src, re.S):
        toks = re.findall(r'"(?:[^"\\]|\\.)*"|\w+', m.group(2))
        consts[m.group(1)] = ''.join(t[1:-1] if t[0] == '"' else consts[t] for t in toks)
    return consts

def run(db, sql, *p):
    return db.execute(sql, p).fetchall()

fallos = 0
def check(nombre, obtenido, esperado):
    global fallos
    ok = obtenido == esperado
    fallos += (not ok)
    print(('OK   ' if ok else 'FALLO'), nombre, '' if ok else f'\n   obtenido={obtenido}\n   esperado={esperado}')

# ---------- classicmodels ----------
c = sqlite3.connect(':memory:')
c.executescript("""
CREATE TABLE offices(officeCode TEXT PRIMARY KEY, city TEXT);
CREATE TABLE employees(employeeNumber INT PRIMARY KEY, lastName TEXT, firstName TEXT, email TEXT, officeCode TEXT, reportsTo INT);
CREATE TABLE customers(customerNumber INT PRIMARY KEY, customerName TEXT, salesRepEmployeeNumber INT);
CREATE TABLE products(productCode TEXT PRIMARY KEY, productName TEXT, quantityInStock INT);
CREATE TABLE orderdetails(orderNumber INT, productCode TEXT, PRIMARY KEY(orderNumber, productCode));
INSERT INTO offices VALUES('4','Paris'),('5','Tokyo'),('9','Madrid-sin-uso'),('8','Rio de Janeiro');
INSERT INTO employees VALUES(1056,'Patterson','Mary','mp@x','1',NULL),(1088,'Patterson','William','wp@x','6',1056),(1166,'Patterson','Steve','sp@x','1',1088);
INSERT INTO employees VALUES(2001,'A','a1','a1@x','4',1056),(2002,'B','b1','b1@x','4',1056),(2003,'C','c1','c1@x','4',1056),(2004,'D','d1','d1@x','4',1056);
INSERT INTO employees VALUES(2005,'E','e1','e1@x','4',1088),(2006,'F','f1','f1@x','4',1088),(2007,'G','g1','g1@x','5',1088);
INSERT INTO employees VALUES(1621,'Marsh','Peter','pm@x','5',1088),(1370,'Hernandez','Gerard','gh@x','5',1056);
INSERT INTO customers VALUES(1,'Kelly''s Gift Shop',1621),(2,'Warburg Exchange',NULL);
INSERT INTO products VALUES('S1','1960 BSA Gold Star DBD34',15),('S2','Otro',300),('S3','Sin pedidos',400),('S4','Grande',9000);
""")
for i in range(28): c.execute("INSERT INTO orderdetails VALUES(?, 'S1')", (i,))
c.execute("INSERT INTO orderdetails VALUES(1,'S2')")

q = sqls(f'{BASE}/jdbc_boletin26/Ejercicio01.java')
check('26-1 empleados Patterson', len(run(c, q['SQL_EMPLEADOS'], 'Patterson')), 3)
check('26-1 Morales no existe', run(c, q['SQL_EMPLEADOS'], 'Morales'), [])
check('26-1 reportan Mary/William/Steve', [len(run(c, q['SQL_REPORTAN'], n)) for n in (1056, 1088, 1166)], [6, 5, 0])  # datos de prueba propios (Mary: 4 + William + Hernandez; William: 5)
q = sqls(f'{BASE}/jdbc_boletin26/Ejercicio02.java')
check('26-2 Kelly -> Peter Marsh', run(c, q['SQL_CLIENTE'], "Kelly's Gift Shop"), [(1621, 'Peter', 'Marsh')])
check('26-2 Warburg sin empleado (fila con NULL)', run(c, q['SQL_CLIENTE'], 'Warburg Exchange'), [(None, None, None)])
check('26-2 cliente Morales inexistente', run(c, q['SQL_CLIENTE'], 'Morales'), [])
check('26-2 empleado 666 inexistente', run(c, q['SQL_EMPLEADO'], 666), [])
check('26-2 empleado 1370', run(c, q['SQL_EMPLEADO'], 1370), [('Gerard', 'Hernandez')])
c.execute(q['SQL_UPDATE'], (1370, 'Warburg Exchange'))
check('26-2 update', run(c, 'SELECT salesRepEmployeeNumber FROM customers WHERE customerNumber=2'), [(1370,)])
q = sqls(f'{BASE}/jdbc_boletin26/Ejercicio03.java')
check('26-3 stock<=500', run(c, q['SQL_STOCK'], 500), [('1960 BSA Gold Star DBD34', 15, 28), ('Otro', 300, 1), ('Sin pedidos', 400, 0)])
check('26-3 stock<=15', run(c, q['SQL_STOCK'], 15), [('1960 BSA Gold Star DBD34', 15, 28)])
check('26-3 stock<=10', run(c, q['SQL_STOCK'], 10), [])
q = sqls(f'{BASE}/jdbc_examenes/Ejercicio01.java')
check('ex1 oficina Paris', run(c, q['SQL_OFICINA'], 'Paris'), [('4',)])
check('ex1 Madrid sin oficina', run(c, q['SQL_OFICINA'], 'Madrid'), [])
check('ex1 empleados Paris', len(run(c, q['SQL_EMPLEADOS'], '4')), 6)
check('ex1 Rio sin empleados', run(c, q['SQL_EMPLEADOS'], '8'), [])
q = sqls(f'{BASE}/jdbc_examenes/Ejercicio02.java')
check('ex2 contar Tokyo', run(c, q['SQL_CONTAR'], '5'), [(3,)])
c.execute(q['SQL_MOVER'], ('4', '5'))
check('ex2 tras mover Tokyo=0, Paris=9', (run(c, q['SQL_CONTAR'], '5'), run(c, q['SQL_CONTAR'], '4')), ([(0,)], [(9,)]))

# ---------- pokemondb ----------
p = sqlite3.connect(':memory:')
p.executescript("""
CREATE TABLE pokemon(numero_pokedex INT PRIMARY KEY, nombre TEXT, peso REAL, altura REAL);
CREATE TABLE evoluciona_de(pokemon_evolucionado INT, pokemon_origen INT);
CREATE TABLE tipo(id_tipo INTEGER PRIMARY KEY, nombre TEXT, generacion INT);
CREATE TABLE pokemon_tipo(numero_pokedex INT, id_tipo INT);
INSERT INTO pokemon VALUES(1,'Bulbasaur',6.9,0.7),(2,'Ivysaur',13,1),(3,'Venusaur',100,2),(151,'Mew',4,0.4),
 (133,'Eevee',6.5,0.3),(134,'Vaporeon',29,1),(135,'Jolteon',24.5,0.8),(136,'Flareon',25,0.9),
 (147,'Dratini',3.3,1.8),(148,'Dragonair',16.5,4),(149,'Dragonite',210,2.2);
INSERT INTO evoluciona_de VALUES(2,1),(3,2),(134,133),(135,133),(136,133);
INSERT INTO tipo VALUES(1,'Dragón',1),(2,'Volador',1),(3,'Acero',1);
INSERT INTO pokemon_tipo VALUES(147,1),(148,1),(149,1),(149,2);
""")
q = sqls(f'{BASE}/jdbc_examenes/Ejercicio03.java')
check('ex3 Bulbasaur existe', run(p, q['SQL_POKEMON'], 'Bulbasaur'), [(1, 'Bulbasaur')])
check('ex3 Sylveon no existe', run(p, q['SQL_POKEMON'], 'Sylveon'), [])
check('ex3 Bulbasaur evoluciona en Ivysaur', run(p, q['SQL_EVOLUCIONES'], 1), [(2, 'Ivysaur')])
check('ex3 Ivysaur viene de Bulbasaur', run(p, q['SQL_ORIGEN'], 2), [(1, 'Bulbasaur')])
check('ex3 Venusaur no evoluciona', run(p, q['SQL_EVOLUCIONES'], 3), [])
check('ex3 Mew nada', (run(p, q['SQL_EVOLUCIONES'], 151), run(p, q['SQL_ORIGEN'], 151)), ([], []))
check('ex3 Eevee 3 evoluciones', [r[1] for r in run(p, q['SQL_EVOLUCIONES'], 133)], ['Vaporeon', 'Jolteon', 'Flareon'])
q = sqls(f'{BASE}/jdbc_examenes/Ejercicio06.java')
check('ex6 tipo Dragón', run(p, q['SQL_TIPO'], 'Dragón'), [(1,)])
check('ex6 tipo Profesor', run(p, q['SQL_TIPO'], 'Profesor'), [])
check('ex6 pokemons dragón', [r[1] for r in run(p, q['SQL_POKEMONS'], 1)], ['Dratini', 'Dragonair', 'Dragonite'])
check('ex6 tipos Dragonite', run(p, q['SQL_TIPOS_DE'], 149), [('Dragón',), ('Volador',)])
check('ex6 Acero sin pokemon', run(p, q['SQL_POKEMONS'], 3), [])

# ---------- naciones ----------
n = sqlite3.connect(':memory:')
n.executescript("""
CREATE TABLE countries(country_id INT PRIMARY KEY, name TEXT);
CREATE TABLE languages(language_id INT PRIMARY KEY, language TEXT);
CREATE TABLE country_languages(country_id INT, language_id INT, official INT);
INSERT INTO countries VALUES(1,'Zimbabwe'),(2,'Zambia'),(3,'Vanuatu');
INSERT INTO languages VALUES(1,'English'),(2,'Ndbele'),(3,'Bemba'),(4,'Bislama'),(5,'Spanish');
INSERT INTO country_languages VALUES(1,1,1),(1,2,0),(2,3,0),(3,1,1),(3,4,1);
""")
q = sqls(f'{BASE}/jdbc_examenes/Ejercicio04.java')
check('ex4 Zimbabwe', run(n, q['SQL_IDIOMAS'], 1), [('English', 1), ('Ndbele', 0)])
check('ex4 país inexistente', run(n, q['SQL_PAIS'], 'Latveria'), [])
q = sqls(f'{BASE}/jdbc_examenes/Ejercicio05.java')
check('ex5 país Zambia', run(n, q['SQL_PAIS'], 'Zambia'), [(2,)])
check('ex5 klingon no existe', run(n, q['SQL_IDIOMA'], 'klingon'), [])
check('ex5 Spanish no se habla en Zambia', run(n, q['SQL_RELACION'], 2, 5), [])
check('ex5 Bislama ya oficial', run(n, q['SQL_RELACION'], 3, 4), [(1,)])
check('ex5 Bemba aún no oficial', run(n, q['SQL_RELACION'], 2, 3), [(0,)])
n.execute(q['SQL_UPDATE'], (2, 3))
check('ex5 update', run(n, q['SQL_RELACION'], 2, 3), [(1,)])
print('\nFALLOS:', fallos); sys.exit(1 if fallos else 0)
