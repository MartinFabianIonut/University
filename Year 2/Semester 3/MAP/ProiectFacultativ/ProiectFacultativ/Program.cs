using ProiectFacultativ.consoleInterface;
using ProiectFacultativ.domain;
using ProiectFacultativ.repository;
using ProiectFacultativ.service;

class Application
{
    public static void Main(String[] args)
    {
        string filepathJucatori = "C:\\GIT\\University\\Year 2\\Semester 3\\MAP\\ProiectFacultativ\\ProiectFacultativ\\data\\jucatori.txt";
        string filepathJucatoriActivi = "C:\\GIT\\University\\Year 2\\Semester 3\\MAP\\ProiectFacultativ\\ProiectFacultativ\\data\\activi.txt";
        string filepathMeci = "C:\\GIT\\University\\Year 2\\Semester 3\\MAP\\ProiectFacultativ\\ProiectFacultativ\\data\\meciuri.txt";
        string filepathEchipe = "C:\\GIT\\University\\Year 2\\Semester 3\\MAP\\ProiectFacultativ\\ProiectFacultativ\\data\\echipe.txt";
        
        AbstractRepository<int, Echipa<int>> echipaRepository = new EchipaRepository<int>(filepathEchipe);
        AbstractRepository<int, Jucator<int>> jucatorRepository = new JucatorRepository<int>(filepathJucatori, (EchipaRepository<int>)echipaRepository);
        AbstractRepository<int, Meci<int>> meciRepository = new MeciRepository<int>(filepathMeci, (EchipaRepository<int>)echipaRepository);
        AbstractRepository<int, JucatorActiv<int>> jucatorActivRepository = new JucatorActivRepository<int>(filepathJucatoriActivi);
        Service<int> service = new Service<int>((MeciRepository<int>)meciRepository, (JucatorRepository<int>)jucatorRepository, (EchipaRepository<int>)echipaRepository, (JucatorActivRepository<int>)jucatorActivRepository);
        
        _ = new Interface<int>(service);
    }
}