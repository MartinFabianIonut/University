package socialnetwork;


import socialnetwork.domain.Friendship;
import socialnetwork.domain.Utilizator;
import socialnetwork.domain.exceptions.RepoException;
import socialnetwork.domain.exceptions.ValidationException;
import socialnetwork.domain.validators.FriendshipValidator;
import socialnetwork.domain.validators.UtilizatorValidator;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repository.file.FriendshipFile0;
import socialnetwork.repository.file.UtilizatorFile0;
import socialnetwork.service.NetworkService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;


public class Main {

    public static BufferedReader ui = new BufferedReader(new InputStreamReader(System.in));
    public static Validator<Utilizator> validator_utilizator = new UtilizatorValidator();
    public static Validator<Friendship> validator_friendship = new FriendshipValidator();
    public static UtilizatorFile0 repo_users = new UtilizatorFile0(Objects.requireNonNull(Main.class.getClassLoader().getResource("users.csv")).getPath(), validator_utilizator);
    public static FriendshipFile0 repo_friendships = new FriendshipFile0(Objects.requireNonNull(Main.class.getClassLoader().getResource("friendships.csv")).getPath(), validator_friendship, repo_users);
    public static NetworkService service = new NetworkService(repo_users, repo_friendships,validator_utilizator,validator_friendship);
    public static void printMeniu(){
        System.out.print("\nAlegeti una dintre optiunile de mai jos:" +
                "\n\t[0] - iesire (or [exit])" +
                "\n-----Utilizatori-----" +
                "\n\t[1] - afisati utilizatorii" +
                "\n\t[2] - adaugati utilizator" +
                "\n\t[3] - stergeti utilizator" +
                "\n-----Prietenii-----" +
                "\n\t[4] - adaugati prietenie" +
                "\n\t[5] - stergeti prietenie" +
                "\n-----Comunitati-----" +
                "\n\t[6] - numarul de comunitati" +
                "\n\t[7] - cea mai sociabila comunitate" +
                "\nOptiune = ");
    }

    public static void printUsers(){
        Iterable<Utilizator> users = repo_users.findAll();
        System.out.println("Lista utilizatorilor existenti:");
        users.forEach(x -> System.out.println("Id = " + x.getId().toString() + ", " + x));
    }

    public static void addUser() throws IOException {
        System.out.print("Introduceti, pe linii noi, urmatoarele date: nume si prenume\n" +
                "Nume = ");
        String lastName, firstName;
        lastName = ui.readLine();
        System.out.print("Prenume = ");
        firstName = ui.readLine();
        service.addUser(lastName,firstName);
        System.out.print("Utilizatorul a fost adaugat cu succes!\n");
    }

    public static void deleteUser() throws IOException {
        printUsers();
        System.out.print("Introduceti id-ul utilizatorului pe care doriti sa il stergeti\nid = ");
        String id = ui.readLine();
        service.deleteUser(Long.parseLong(id));
        System.out.print("Utilizatorul cu id = " + id + " a fost eliminat cu succes!\n");
    }

    public static void addFriendship() throws Exception {
        printUsers();
        System.out.print("Introduceti id-urile a doi utilizatori pentru a crea prietenie intre acestia:\nid1 = ");
        String id1, id2;
        id1 = ui.readLine();
        System.out.print("id2 = ");
        id2 = ui.readLine();
        service.addFriendship(Long.parseLong(id1), Long.parseLong(id2));
        System.out.print("Prietenia intre utilizatorul cu id1 = " + id1+ " si cel cu id2 = " + id2 + " a fost adaugata cu succes!\n");
        //repo.addFriendship(u1,u2);
    }

    public static void deleteFriendship() throws IOException{
        printUsers();
        System.out.print("Introduceti id-urile a doi utilizatori pentru a sterge prietenia dintre acestia:\nid1 = ");
        String id1, id2;
        id1 = ui.readLine();
        System.out.print("id2 = ");
        id2 = ui.readLine();
        service.deleteFriendship(Long.parseLong(id1), Long.parseLong(id2));
        System.out.print("Prietenia intre utilizatorul cu id1 = " + id1+ " si cel cu id2 = " + id2 + " a fost eliminata cu succes!\n");
    }

    public static void numberOfCommunities(){
        System.out.println("In reteaua curenta, comunitatile sunt in numar de = " + service.numberOfCommunities());
    }

    public static void mostSociableCommunity(){
        System.out.println("In reteaua curenta, comunitatea cea mai sociabila este compusa din urmatorii utilizatori: ");
        Iterable<Utilizator> list = service.getMostSociableCommunity();
        for(Utilizator u: list)
            System.out.println(u);
    }

    public static void main(String[] args) throws Exception {
        boolean exit = true;
        do {
            printMeniu();
            String option = ui.readLine();
            switch (option) {
                case "1" -> printUsers();
                case "2" -> {
                    try{
                        addUser();
                    }catch (ValidationException e){
                        System.out.println("Eroare la adaugare utilizator: " + e);
                    }
                }
                case "3" -> {
                    try{
                        deleteUser();
                    }catch (RepoException e){
                        System.out.println("Eroare la stergere utilizator: " + e);
                    }
                }
                case "4" -> {
                    try{
                        addFriendship();
                    }catch (RepoException e){
                        System.out.println("Eroare la adaugare prietenie: " + e);
                    }
                }
                case "5" -> {
                    try{
                        deleteFriendship();
                    }catch (RepoException e){
                        System.out.println("Eroare la stergere prietenie: " + e);
                    }
                }
                case "6" -> numberOfCommunities();
                case "7" -> mostSociableCommunity();
                case "0", "exit" -> exit = false;
                default -> {
                    System.out.println("Comanda invalida!");
                }
            }
        } while (exit);
    }
}


