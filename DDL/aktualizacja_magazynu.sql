CREATE OR REPLACE FUNCTION rozpakuj_zamowienie_na_magazyn(p_id_zamowienia INTEGER, p_id_sklepu INTEGER) RETURNS void AS
    $BODY$
    DECLARE
        v_id_produktu INTEGER;
        v_ilosc       INTEGER;
    BEGIN
        FOR v_id_produktu, v_ilosc IN SELECT id_produktu, ilosc FROM pozycje WHERE pozycje.id_zamowienia = p_id_zamowienia
        LOOP
            UPDATE stan_magazynowy
            SET ilosc = ilosc + v_ilosc
            WHERE stan_magazynowy.id_produktu = v_id_produktu AND stan_magazynowy.id_sklepu = p_id_sklepu;
        END LOOP;
    END;
    $BODY$
LANGUAGE plpgsql VOLATILE;

CREATE OR REPLACE FUNCTION aktualizuj_magazyn_po_sprzedazy(p_id_dowodu_sprzedazy INTEGER, p_id_sklepu INTEGER) RETURNS void AS
$BODY$
DECLARE
    v_id_produktu INTEGER;
    v_ilosc       INTEGER;
BEGIN
    FOR v_id_produktu, v_ilosc IN SELECT id_produktu, ilosc FROM sprzedane_produkty WHERE sprzedane_produkty.id_dowodu_sprzedazy = p_id_dowodu_sprzedazy
    LOOP
        UPDATE stan_magazynowy
        SET ilosc = ilosc - v_ilosc
        WHERE stan_magazynowy.id_produktu = v_id_produktu AND stan_magazynowy.id_sklepu = p_id_sklepu;
    END LOOP;
END;
$BODY$
LANGUAGE plpgsql VOLATILE;