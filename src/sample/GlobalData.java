package sample;

import com.sample.Klienci;
import com.sample.Produkty;
import com.sample.Sklepy;
import com.sample.Sprzedawcy;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GlobalData {
    private static Connection db;
    public static Long sellerID;
    public static Long shopID;
    public static Long customerID;

    public static void connectWithDatabase() {
        System.out.println("Łączę się z bazą danych...");
        db = ConnectionConfiguration.getConnection();
    }

    public static void closeDatabeseConnection() {
        try {
            System.out.println("Zamykam połączenie z bazą danych...");
            db.close();
        } catch (java.sql.SQLException e) {
            System.out.println("Problem w closeDatabaseConnection()");
            System.out.println(e.getMessage());
        }
    }

    public static List<KeyValuePair> selectShops() {
        System.out.println("Odpalam selectShops...");
        List<KeyValuePair> shops = new ArrayList<>();

        try {
            Statement st = db.createStatement();
            ResultSet rs = st.executeQuery("SELECT id_sklepu, adres FROM sklepy");
            while (rs.next()) {
                KeyValuePair oneShop = new KeyValuePair(rs.getLong("id_sklepu"), rs.getString("adres"));
//                oneShop.setIdSklepu(rs.getLong("id_sklepu"));
//                oneShop.setAdres(rs.getString("adres"));
                shops.add(oneShop);
            }
            rs.close();
            st.close();
        } catch (java.sql.SQLException e) {
            System.out.println("Problem w selectShops()");
            System.out.println(e.getMessage());
        }
        return  shops;
    }

    public static List<KeyValuePair> selectSellers() {
        System.out.println("Odpalam selectSellers...");
        List<KeyValuePair> sellers = new ArrayList<>();

        try {
            PreparedStatement preparedStatement = db.prepareStatement("SELECT * FROM sprzedawcy WHERE id_sklepu = ? AND widoczny='TAK' ORDER BY nazwisko");
            preparedStatement.setLong(1, shopID);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                String temp = rs.getString("imie");
                temp += " ";
                temp += rs.getString("nazwisko");
                KeyValuePair oneSeller = new KeyValuePair(rs.getLong("id_sprzedawcy"), temp);
                sellers.add(oneSeller);
            }
            rs.close();
            preparedStatement.close();
        } catch (java.sql.SQLException e) {
            System.out.println("Problem w selectSellers()");
            System.out.println(e.getMessage());
        }
        return  sellers;
    }

    public static List<String> selectProducers() {
        System.out.println("Odpalam selectProducers...");
        List<String> producers = new ArrayList<>();

        try {
            PreparedStatement preparedStatement = db.prepareStatement("SELECT * FROM firmy");
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                producers.add(rs.getString("nazwa"));
            }
            rs.close();
            preparedStatement.close();
        } catch (java.sql.SQLException e) {
            System.out.println("Problem w selectProducers()");
            System.out.println(e.getMessage());
        }
        return  producers;
    }


    public static List<KeyValuePair> selectProducerProduct(String producerName) {
        System.out.println("Odpalam selectProducersProduct...");
        List<KeyValuePair> products = new ArrayList<>();

        try {
            PreparedStatement preparedStatement = db.prepareStatement("SELECT produkty.id_produktu as idProduktu, produkty.nazwa as nazwaProduktu FROM produkty join firmy on produkty.nip = firmy.nip where firmy.nazwa = ? AND produkty.w_ofercie='t' ORDER BY produkty.nazwa, produkty.id_produktu");
            preparedStatement.setString(1, producerName);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                KeyValuePair oneProduct = new KeyValuePair(rs.getLong("idProduktu"), rs.getString("nazwaProduktu"));
                products.add(oneProduct);
            }
            rs.close();
            preparedStatement.close();
            System.out.println("wykonano");
        } catch (java.sql.SQLException e) {
            System.out.println("Problem w selectProducersPtoduct()");
            System.out.println(e.getMessage());
        }
        return  products;
    }

    public static List<String> selectCategory() {
        System.out.println("Odpalam selectCategory...");
        List<String> category = new ArrayList<>();

        try {
            PreparedStatement preparedStatement = db.prepareStatement("SELECT * FROM kategorie");
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                category.add(rs.getString("nazwa"));
            }
            rs.close();
            preparedStatement.close();
        } catch (java.sql.SQLException e) {
            System.out.println("Problem w selectCategory()");
            System.out.println(e.getMessage());
        }
        return  category;
    }

    public static List<com.sample.DowodySprzedazy> selectClientBills(long id, String typeOfBill) {
        System.out.println("Odpalam selectClientBills...");
        List<com.sample.DowodySprzedazy> bills = new ArrayList<com.sample.DowodySprzedazy>();

        try {
            String sql = "SELECT * FROM dowody_sprzedazy WHERE id_klienta = ?";
            if(typeOfBill.equals("both")) {
                sql += " AND 1=1";
            }
            else if(typeOfBill.equals("receipt")) {
                sql += " AND nr_paragonu IS NOT NULL";
            }
            else if(typeOfBill.equals("invoice")) {
                sql += " AND nr_faktury IS NOT NULL";
            }
            else {
                sql += " AND 2=1";
            }
            sql += " order by data";
            PreparedStatement preparedStatement = db.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                com.sample.DowodySprzedazy dowod = new com.sample.DowodySprzedazy();
                dowod.setIdDowoduSprzedazy(rs.getLong("id_dowodu_sprzedazy"));
                dowod.setData(rs.getDate("data"));
                dowod.setNrFaktury(rs.getString("nr_paragonu"));
                dowod.setNrParagonu(rs.getString("nr_faktury"));
                dowod.setKwota(rs.getString("kwota"));
                bills.add(dowod);
            }
            rs.close();
            preparedStatement.close();
        } catch (java.sql.SQLException e) {
            System.out.println("Problem w selectclientBills()");
            System.out.println(e.getMessage());
        }
        return  bills;
    }

    private static void prepareProductsList(List<Produkty> products, ResultSet rs) throws SQLException {
        while (rs.next()) {
            Produkty oneProduct = new Produkty();
            oneProduct.setIdProduktu(rs.getLong("id_produktu"));
            oneProduct.setNazwa(rs.getString("produkt"));
            float priceTemp = rs.getFloat("cena");
            oneProduct.setCena(String.format("%.2f", priceTemp));
            oneProduct.setKategorieNazwa(rs.getString("kategoria"));
            oneProduct.setNazwaFirmy(rs.getString("firma"));
            products.add(oneProduct);
        }
    }

    private static void prepareProductsListToShow(List<Produkty> products, ResultSet rs) throws SQLException {
        while (rs.next()) {

            Produkty oneProduct = new Produkty();
            oneProduct.setIdProduktu(rs.getInt("id_produktu"));
            oneProduct.setNazwa(rs.getString("produkt"));
            if(rs.getString("ilosc") == null){
                oneProduct.setIloscWSklepie(0);
            }else {
                oneProduct.setIloscWSklepie(rs.getInt("ilosc"));
            }
            float priceTemp = rs.getFloat("cena");
            oneProduct.setCena(String.format("%.2f", priceTemp));
            oneProduct.setKategorieNazwa(rs.getString("kategoria"));
            oneProduct.setNazwaFirmy(rs.getString("firma"));
            products.add(oneProduct);
        }
    }

    private static void prepareSellersList(List<Sprzedawcy> sellers, ResultSet rs) throws SQLException {
        while (rs.next()) {
            Sprzedawcy oneSeller = new Sprzedawcy();
            oneSeller.setIdSprzedawcy(rs.getLong("id_sprzedawcy"));
            oneSeller.setImie(rs.getString("imie"));
            oneSeller.setNazwisko(rs.getString("nazwisko"));
            oneSeller.setNrTelefonu(rs.getLong("nr_telefonu"));
            oneSeller.setAdres_sklepu(rs.getString("adres"));
            sellers.add(oneSeller);
        }
    }

    public static List<com.sample.Sprzedawcy> selectSellersToFire(Long id_sklepu, String pattern) {
        List<com.sample.Sprzedawcy> sellers = new ArrayList<>();
        try {
            pattern = pattern
                    .replace("!", "!!")
                    .replace("%", "!%")
                    .replace("_", "!_")
                    .replace("[", "![");

            String sql = "SELECT id_sprzedawcy, imie, nazwisko, nr_telefonu, adres" +
                    " FROM sprzedawcy join sklepy on sprzedawcy.id_sklepu = sklepy.id_sklepu" +
                    " WHERE sprzedawcy.widoczny='TAK'";

            int index = 1;
            if(id_sklepu != null) {
                sql += " AND sprzedawcy.id_sklepu=?";
                index = 2;
            }
            if(!pattern.isEmpty()) {
                sql += " AND LOWER(nazwisko) LIKE LOWER(?) ESCAPE '!'";
            }
            sql += " ORDER BY nazwisko, imie, id_sprzedawcy";
            System.out.println(sql);
            PreparedStatement preparedStatement = db.prepareStatement(sql);
            if(id_sklepu != null) preparedStatement.setLong(1, id_sklepu);
            if(!pattern.isEmpty()) preparedStatement.setString(index, pattern+"%");
            ResultSet rs = preparedStatement.executeQuery();
            prepareSellersList(sellers, rs);
            rs.close();
            preparedStatement.close();
        } catch (java.sql.SQLException e) {
            System.out.println("Problem w selectSellersToFire()");
            System.out.println(e.getMessage());
        }
        return sellers;
    }

    public static void updateShopInventory(List<com.sample.Pozycje> positionsInNewSupply){
        for(com.sample.Pozycje position : positionsInNewSupply){
            try{
                PreparedStatement preparedStatement = db.prepareStatement("UPDATE stan_magazynowy set ilosc = ilosc + ? " +
                        "where id_produktu = ? and id_sklepu = ?");
                preparedStatement.setLong(1, position.getIlosc());
                preparedStatement.setLong(2, position.getIdProduktu());
                preparedStatement.setLong(3, shopID);
                preparedStatement.executeUpdate();
                System.out.println("Wprowadzono: id-" +position.getIdProduktu() + " ilosc-" + position.getIlosc() + " shopid-" + shopID );

            }catch(java.sql.SQLException e){
                System.out.println("Problem w updateShopInventory()");
                System.out.println(e.getMessage());
            }
        }
        System.out.println("Wprowadzono dostawe.");
    }

    private static void prepareClientsList(List<Klienci> clients, ResultSet rs) throws SQLException {
        while (rs.next()) {
            Klienci oneClient = new Klienci();
            oneClient.setIdKlienta(rs.getLong("id_klienta"));
            oneClient.setImie(rs.getString("imię"));
            oneClient.setNazwisko(rs.getString("nazwisko"));
            oneClient.setEmail(rs.getString("email"));
            clients.add(oneClient);
        }
    }

    public static List<com.sample.Klienci> selectClients(String emailPattern, String surnamePattern) {
        List<com.sample.Klienci> clients = new ArrayList<>();
        try {
            emailPattern = emailPattern
                    .replace("!", "!!")
                    .replace("%", "!%")
                    .replace("_", "!_")
                    .replace("[", "![");

            surnamePattern = surnamePattern
                    .replace("!", "!!")
                    .replace("%", "!%")
                    .replace("_", "!_")
                    .replace("[", "![");



            String sql = "SELECT id_klienta, imię, nazwisko, email FROM klienci";

            if(!emailPattern.isEmpty() || !surnamePattern.isEmpty())
                sql += " WHERE";
            int index = 1;
            if(!emailPattern.isEmpty()) {
                sql += " LOWER(email) LIKE LOWER(?) ESCAPE '!'";
                index = 2;
            }
            if(!surnamePattern.isEmpty()) {
                if(index == 2) {
                    sql += " AND";
                }
                sql += " LOWER(nazwisko) LIKE LOWER(?) ESCAPE '!'";
            }
            sql += " ORDER BY nazwisko, imię, id_klienta";
            System.out.println(sql);
            PreparedStatement preparedStatement = db.prepareStatement(sql);
            if(!emailPattern.isEmpty()) preparedStatement.setString(1, "%"+emailPattern+"%");
            if(!surnamePattern.isEmpty()) preparedStatement.setString(index, surnamePattern+"%");
            ResultSet rs = preparedStatement.executeQuery();
            prepareClientsList(clients, rs);
            rs.close();
            preparedStatement.close();
        } catch (java.sql.SQLException e) {
            System.out.println("Problem w selectSellersToFire()");
            System.out.println(e.getMessage());
        }
        return clients;
    }



    public static List<com.sample.Produkty> selectAllProducts() {
        List<com.sample.Produkty> products = new ArrayList<>();
        try {
            Statement st = db.createStatement();
            ResultSet rs = st.executeQuery("SELECT id_produktu, produkty.nazwa AS produkt, cena, kategorie_nazwa AS kategoria, firmy.nazwa AS firma" +
                    " FROM produkty join firmy on produkty.nip = firmy.nip" +
                    " WHERE w_ofercie='t'" +
                    " ORDER BY produkty.nazwa, id_produktu");
            prepareProductsList(products, rs);
            rs.close();
            st.close();
        } catch (java.sql.SQLException e) {
            System.out.println("Problem w selectAllProducts()");
            System.out.println(e.getMessage());
        }
        return products;
    }

    public static List<com.sample.Produkty> selectAllProductsToShow() {
        List<com.sample.Produkty> products = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = db.prepareStatement("SELECT produkty.id_produktu as id_produktu, ilosc, produkty.nazwa AS produkt, cena, kategorie_nazwa AS kategoria, firmy.nazwa AS firma" +
                    " FROM produkty inner join firmy on produkty.nip = firmy.nip inner join stan_magazynowy on produkty.id_produktu = stan_magazynowy.id_produktu" +
                    " WHERE w_ofercie='t' and id_sklepu = ?" +
                    " ORDER BY produkty.nazwa, id_produktu");
            preparedStatement.setLong(1, shopID);
            ResultSet rs = preparedStatement.executeQuery();
            prepareProductsListToShow(products, rs);
            rs.close();
            preparedStatement.close();
        } catch (java.sql.SQLException e) {
            System.out.println("Problem w selectAllProductsToShow()");
            System.out.println(e.getMessage());
        }
        return products;
    }

    public static List<com.sample.Produkty> selectAllAvailableProducts(String category, String pattern) {
        List<com.sample.Produkty> products = new ArrayList<>();
        try {
            pattern = pattern
                    .replace("!", "!!")
                    .replace("%", "!%")
                    .replace("_", "!_")
                    .replace("[", "![");

            String sql = "SELECT produkty.id_produktu as id_produktu, ilosc, produkty.nazwa AS produkt, cena, kategorie_nazwa AS kategoria, firmy.nazwa AS firma" +
                    " FROM produkty inner join firmy on produkty.nip = firmy.nip inner join stan_magazynowy on produkty.id_produktu = stan_magazynowy.id_produktu" +
                    " WHERE id_sklepu = ? and stan_magazynowy.ilosc > 0";
            int index = 2;
            if(category != null) {
                sql += " AND kategorie_nazwa = ?";
                index = 3;
            }
            if(!pattern.isEmpty()) {
                sql += " AND LOWER(produkty.nazwa) LIKE LOWER(?) ESCAPE '!'";
            }
            sql += " ORDER BY produkty.nazwa, id_produktu";
            System.out.println(sql);


            PreparedStatement preparedStatement = db.prepareStatement(sql);
            preparedStatement.setLong(1, shopID);
            if(category != null) preparedStatement.setString(2, category);
            if(!pattern.isEmpty()) preparedStatement.setString(index, "%"+pattern+"%");
            ResultSet rs = preparedStatement.executeQuery();
            prepareProductsListToShow(products, rs);
            rs.close();
            preparedStatement.close();
        } catch (java.sql.SQLException e) {
            System.out.println("Problem w selectAllAvailableProducts()");
            System.out.println(e.getMessage());
        }
        return products;
    }

    public static List<com.sample.Produkty> selectProductsFiltered(String category, String pattern) {
        List<com.sample.Produkty> products = new ArrayList<>();
        pattern = pattern
                .replace("!", "!!")
                .replace("%", "!%")
                .replace("_", "!_")
                .replace("[", "![");
        try {
            String sql = "SELECT id_produktu, produkty.nazwa AS produkt, cena, kategorie_nazwa AS kategoria, firmy.nazwa AS firma FROM produkty join firmy on produkty.nip = firmy.nip WHERE w_ofercie='t'";

            int index = 1;
            if(category != null) {
                sql += " AND kategorie_nazwa = ?";
                index = 2;
            }
            if(!pattern.isEmpty()) {
                sql += " AND LOWER(produkty.nazwa) LIKE LOWER(?) ESCAPE '!'";
            }
            sql += " ORDER BY produkty.nazwa, id_produktu";
            System.out.println(sql);
            PreparedStatement preparedStatement = db.prepareStatement(sql);
            if(category != null) preparedStatement.setString(1, category);
            if(!pattern.isEmpty()) preparedStatement.setString(index, "%"+pattern+"%");
            ResultSet rs = preparedStatement.executeQuery();
            prepareProductsList(products, rs);
            rs.close();
            preparedStatement.close();
        } catch (java.sql.SQLException e) {
            System.out.println("Problem w selectProductsByCategory()");
            System.out.println(e.getMessage());
        }
        return products;
    }

    public static List<com.sample.Produkty> selectProductsInShopFiltered(String category, String pattern) {
        List<com.sample.Produkty> products = new ArrayList<>();
        pattern = pattern
                .replace("!", "!!")
                .replace("%", "!%")
                .replace("_", "!_")
                .replace("[", "![");
        try {
            String sql = "SELECT produkty.id_produktu as id_produktu, produkty.nazwa AS produkt, ilosc, cena, kategorie_nazwa AS kategoria, firmy.nazwa AS firma"
            + " FROM produkty inner join firmy on produkty.nip = firmy.nip inner join stan_magazynowy on produkty.id_produktu = stan_magazynowy.id_produktu WHERE w_ofercie='t' and id_sklepu = ?";

            int index = 1;
            if(category != null) {
                sql += " AND kategorie_nazwa = ?";
                index = 2;
            }
            if(!pattern.isEmpty()) {
                sql += " AND LOWER(produkty.nazwa) LIKE LOWER(?) ESCAPE '!'";
            }
            sql += " ORDER BY produkty.nazwa, stan_magazynowy.id_produktu";
            System.out.println(sql);
            PreparedStatement preparedStatement = db.prepareStatement(sql);
            if(category != null) preparedStatement.setString(2, category);
            if(!pattern.isEmpty()) preparedStatement.setString(index, "%"+pattern+"%");
            preparedStatement.setLong(1, shopID);
            ResultSet rs = preparedStatement.executeQuery();
            prepareProductsListToShow(products, rs);
            rs.close();
            preparedStatement.close();
        } catch (java.sql.SQLException e) {
            System.out.println("Problem w selectProductsInShop");
            System.out.println(e.getMessage());
        }
        return products;
    }

    public static void removeProduct(Long id_produktu) {
        System.out.println("Odpalam removeProduct...");
        try {
            PreparedStatement preparedStatement = db.prepareStatement("UPDATE produkty SET w_ofercie='n' WHERE id_produktu=?");
            preparedStatement.setLong(1, id_produktu);

            preparedStatement.executeUpdate();
            preparedStatement.close();

        }catch (java.sql.SQLException e) {
            System.out.println("Problem w removeProduct()");
            System.out.println(e.getMessage());
        }
    }

    public static void fireSeller(Long id_sprzedawcy) {
        System.out.println("Odpalam fireSeller...");
        try {
            PreparedStatement preparedStatement = db.prepareStatement("UPDATE sprzedawcy SET widoczny='NIE' WHERE id_sprzedawcy=?");
            preparedStatement.setLong(1, id_sprzedawcy);

            preparedStatement.executeUpdate();
            preparedStatement.close();

        }catch (java.sql.SQLException e) {
            System.out.println("Problem w fireSeller()");
            System.out.println(e.getMessage());
        }
    }

    public static void insertNewSaleEvidenceP(String receiptNumber, Long nip, String adres, double price, List<com.sample.Pozycje> products){
        System.out.println("Odpalam insertNewSaleEvidenceP...");
        ResultSet rs;
        PreparedStatement preparedStatement;
        try {
            db.setAutoCommit(false);
            Integer idRachunku;
            Statement st = db.createStatement();
            rs = st.executeQuery("SELECT nextval('dowody_sprzedarzy_seq') AS id_rachunku");
            rs.next();
            idRachunku = rs.getInt("id_rachunku");
            rs.close();
            st.close();

            if(receiptNumber.startsWith("FAV")) {

                if(nip != null && adres != null) {
                    preparedStatement = db.prepareStatement("INSERT INTO dowody_sprzedazy(id_dowodu_sprzedazy, kwota, id_sprzedawcy, nr_faktury , id_klienta, nip, adres)" + " VALUES (?, ?, ?, ?, ?, ?, ?)");
                    preparedStatement.setLong(6, nip);
                    preparedStatement.setString(7, adres);
                }
                else if(nip != null && adres == null)
                {
                    preparedStatement = db.prepareStatement("INSERT INTO dowody_sprzedazy(id_dowodu_sprzedazy, kwota, id_sprzedawcy, nr_faktury , id_klienta, nip)" + " VALUES (?, ?, ?, ?, ?, ?)");
                    preparedStatement.setLong(6, nip);
                }
                else if(nip == null && adres != null) {
                    preparedStatement = db.prepareStatement("INSERT INTO dowody_sprzedazy(id_dowodu_sprzedazy, kwota, id_sprzedawcy, nr_faktury , id_klienta, adres)" + " VALUES (?, ?, ?, ?, ?, ?)");
                    preparedStatement.setString(6, adres);
                }
                else {
                    preparedStatement = db.prepareStatement("INSERT INTO dowody_sprzedazy(id_dowodu_sprzedazy, kwota,id_sprzedawcy, nr_faktury , id_klienta)" + " VALUES (?, ?, ?, ?, ?)");
                }

                preparedStatement.setInt(1, idRachunku);
                preparedStatement.setDouble(2, price);
                if(customerID!=null) {
                    preparedStatement.setLong(5, customerID);
                }
                else {
                    preparedStatement.setNull(5, Types.INTEGER);
                }
                preparedStatement.setLong(3, sellerID);
                preparedStatement.setString(4, receiptNumber);
            }
            else {
                if(customerID!=null){
                    preparedStatement = db.prepareStatement("INSERT INTO dowody_sprzedazy(id_dowodu_sprzedazy, kwota,id_sprzedawcy, nr_paragonu , id_klienta)" + " VALUES (?, ?, ?, ?, ?)");
                }
                else{
                    preparedStatement = db.prepareStatement("INSERT INTO dowody_sprzedazy(id_dowodu_sprzedazy, kwota, id_sprzedawcy, nr_paragonu)" + " VALUES ( ?, ?, ?, ?)");
                }
                preparedStatement.setInt(1, idRachunku);
                preparedStatement.setDouble(2, price);
                if(customerID!=null)preparedStatement.setLong(5, customerID);
                preparedStatement.setLong(3, sellerID);
                preparedStatement.setString(4, receiptNumber);
            }

            preparedStatement.executeUpdate();
            preparedStatement.close();

            preparedStatement = db.prepareStatement("INSERT INTO sprzedane_produkty(ilosc,id_dowodu_sprzedazy, id_produktu) VALUES(?, ?, ?)");
            preparedStatement.setLong(2, idRachunku);
            for(com.sample.Pozycje oneProduct : products) {
                preparedStatement.setLong(1, oneProduct.getIlosc());
                preparedStatement.setLong(3, oneProduct.getIdProduktu());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
            preparedStatement.close();
            db.commit();
            db.setAutoCommit(true);

            CallableStatement callableStatement = db.prepareCall("{CALL aktualizuj_magazyn_po_sprzedazy(?, ?)}");
            callableStatement.setInt(1, idRachunku);
            callableStatement.setInt(2, shopID.intValue());
            callableStatement.execute();
            callableStatement.close();


        }catch (java.sql.SQLException e) {
            System.out.println("Problem w insertNewSaleEvidenceP()");
            System.out.println(e.getMessage());
        }
    }


    public static void insertNewCustomer(String name, String surname, String email){
        System.out.println("Odpalam insertNewCustomer...");
        try {
            if(email.length() < 2){
                PreparedStatement preparedStatement = db.prepareStatement("INSERT INTO klienci (imię, nazwisko)"+"VALUES (?, ?)");
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, surname);
                preparedStatement.executeUpdate();
                preparedStatement.close();
            }
            else{
                PreparedStatement preparedStatement = db.prepareStatement("INSERT INTO klienci (imię, nazwisko, email)"+" VALUES (?, ?, ?)");
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, surname);
                preparedStatement.setString(3, email);
                preparedStatement.executeUpdate();
                preparedStatement.close();
            }
        }catch (java.sql.SQLException e) {
            System.out.println("Problem w insertNewCustomer()");
            System.out.println(e.getMessage());
        }
    }

    public static void insertNewProduct(String name, Float price, String producer, String category){
        System.out.println("Odpalam insertNewProduct...");
        String producerId;
        int insertedProductId;
        try {
            PreparedStatement preparedStatement = db.prepareStatement("SELECT * FROM firmy WHERE nazwa = ?");
            preparedStatement.setString(1, producer);
            ResultSet rs = preparedStatement.executeQuery();
            rs.next();
            producerId = rs.getString("nip");
            rs.close();
            preparedStatement.close();

            preparedStatement = db.prepareStatement("INSERT INTO produkty (nazwa, cena, nip, kategorie_nazwa, w_ofercie)"+" VALUES (?, ?, ?, ?, ?)");
            preparedStatement.setString(1, name);
            preparedStatement.setFloat(2, price);
            preparedStatement.setLong(3, Long.valueOf(producerId));
            preparedStatement.setString(4, category);
            preparedStatement.setString(5, "t");

            preparedStatement.executeUpdate();
            preparedStatement.close();

            preparedStatement = db.prepareStatement("select last_value as product_id from produkty_seq");
            rs = preparedStatement.executeQuery();
            rs.next();
            insertedProductId = rs.getInt("product_id");
            rs.close();
            preparedStatement.close();

            preparedStatement = db.prepareStatement("insert into stan_magazynowy(id_sklepu,id_produktu,ilosc)\n" +
                    "select id_sklepu , ?, 0 from sklepy");
            preparedStatement.setInt(1, insertedProductId);
            preparedStatement.executeUpdate();
            preparedStatement.close();

        }catch (java.sql.SQLException e) {
            System.out.println("Problem w insertNewProduct()");
            System.out.println(e.getMessage());
        }
    }

    public static boolean insertNewCategory(String category){
        System.out.println("Odpalam insertNewCategory...");
        boolean insertOK = true;
        try {
            PreparedStatement preparedStatement = db.prepareStatement("INSERT INTO kategorie (nazwa)" + " VALUES (?)");
            preparedStatement.setString(1, category);

            preparedStatement.executeUpdate();
            preparedStatement.close();

        }catch (java.sql.SQLException e) {
            if (e.getSQLState().startsWith("23")){
                insertOK = false;
                System.out.println("Nie duplikujemy");
            }
            else {
                System.out.println("Problem w insertNewCategory()");
                System.out.println(e.getMessage());
            }
        }
        return insertOK;
    }

    public static void insertNewShop(String addres){
        System.out.println("Odpalam insertNewShop...");
        try {
            int newShopId;
            PreparedStatement preparedStatement = db.prepareStatement("INSERT INTO sklepy (adres)"+" VALUES (?)");
            preparedStatement.setString(1, addres);
            preparedStatement.executeUpdate();
            preparedStatement.close();

            preparedStatement = db.prepareStatement("select last_value as shop_id from sklepy_seq");
            ResultSet rs = preparedStatement.executeQuery();
            rs.next();
            newShopId = rs.getInt("shop_id");
            rs.close();
            preparedStatement.close();

            preparedStatement = db.prepareStatement("insert into stan_magazynowy(id_sklepu,id_produktu,ilosc)\n" +
                    "select ?, id_produktu, 0 from produkty");
            preparedStatement.setInt(1, newShopId);
            preparedStatement.executeUpdate();
            preparedStatement.close();

        }catch (java.sql.SQLException e) {
            System.out.println("Problem w insertNewShop()");
            System.out.println(e.getMessage());
        }
    }

    public static void insertNewSeller(String name, String surname, Long phoneNumber, Long newSellerShopId) {
        System.out.println("Odpalam insertNewSeller...");
        try {
            PreparedStatement preparedStatement = db.prepareStatement("INSERT INTO sprzedawcy (imie, nazwisko, nr_telefonu, id_sklepu, widoczny)"+" VALUES (?, ?, ?, ?, 'TAK')");
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, surname);
            if(phoneNumber == null) {
                preparedStatement.setNull(3, Types.INTEGER);
            }
            else {
                preparedStatement.setLong(3, phoneNumber);
            }
            preparedStatement.setLong(4, newSellerShopId);
            preparedStatement.executeUpdate();
            preparedStatement.close();

        }catch (java.sql.SQLException e) {
            System.out.println("Problem w insertNewSeller()");
            System.out.println(e.getMessage());
        }
    }

    public static void insertNewOrder(String producerName, List<com.sample.Pozycje> positionsInNewSupply) {
        ResultSet rs;
        PreparedStatement preparedStatement;
        try {
            db.setAutoCommit(false);
            Integer id_zamowienia;
            Statement st = db.createStatement();
            rs = st.executeQuery("SELECT nextval('zamowienia_seq') AS id_zamowienia");
            rs.next();
            id_zamowienia = rs.getInt("id_zamowienia");
            rs.close();
            st.close();

            Long nip;
            preparedStatement = db.prepareStatement("SELECT nip FROM firmy WHERE firmy.nazwa = ?");
            preparedStatement.setString(1, producerName);
            rs = preparedStatement.executeQuery();
            rs.next();
            nip = rs.getLong("nip");
            rs.close();
            preparedStatement.close();


            preparedStatement = db.prepareStatement("INSERT INTO zamowienia(id_zamowienia, id_sklepu, nip)VALUES (?, ?, ?)");
            preparedStatement.setInt(1, id_zamowienia);
            preparedStatement.setLong(2,shopID);
            preparedStatement.setLong(3, nip);
            preparedStatement.executeUpdate();
            preparedStatement.close();

            preparedStatement = db.prepareStatement("INSERT INTO pozycje(ilosc, id_zamowienia, id_produktu) VALUES (?, ?, ?)");
            preparedStatement.setInt(2, id_zamowienia);
            for(com.sample.Pozycje onePosition : positionsInNewSupply) {
                System.out.println(onePosition.getIdProduktu());
                System.out.println(onePosition.getIlosc());
                System.out.println("\n");
                preparedStatement.setLong(1, onePosition.getIlosc());
                preparedStatement.setLong(3, onePosition.getIdProduktu());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
            preparedStatement.close();
            db.commit();
            db.setAutoCommit(true);

            CallableStatement callableStatement = db.prepareCall("{CALL rozpakuj_zamowienie_na_magazyn(?, ?)}");
            callableStatement.setInt(1, id_zamowienia);
            callableStatement.setInt(2, shopID.intValue());
            callableStatement.execute();
            callableStatement.close();

        }catch (java.sql.SQLException e){
            System.out.println("Problem w insertNewOrder()");
            System.out.println(e.getMessage());
        }
    }

    public static String getBillNumber(String type) {
        String returnValue = "";
        try {
            CallableStatement callableStatement = db.prepareCall("{? = CALL numer_rachunku(?)}");
            callableStatement.setString(2, type);
            callableStatement.registerOutParameter(1, Types.VARCHAR);
            callableStatement.execute();
            returnValue = callableStatement.getString(1);
            callableStatement.close();
        }catch (java.sql.SQLException e) {
            System.out.println("Problem w getBillNumber()");
            System.out.println(e.getMessage());
        }
        return returnValue;
    }

}
