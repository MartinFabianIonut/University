using ProiectFacultativ.domain;
using ProiectFacultativ.service;

namespace ProiectFacultativ.consoleInterface
{
    internal class Interface<ID>
    {
        private Service<ID> service;
        public Interface(Service<ID> service)
        {
            this.service = service;
            Run();
        }
        private delegate void executeTaskDelegate();

        private void Run()
        {
            executeTaskDelegate taskExecuter;
            while (true)
            {
                Console.Write(">>");
                string option = Console.ReadLine();
                switch (option)
                {
                    case "1":
                        taskExecuter = new executeTaskDelegate(Task1);
                        break;
                    case "2":
                        taskExecuter = new executeTaskDelegate(Task2);
                        break;
                    case "3":
                        taskExecuter = new executeTaskDelegate(Task3);
                        break;
                    case "4":
                        taskExecuter = new executeTaskDelegate(Task4);
                        break;
                    case "0" or "exit":
                        Environment.Exit(0);
                        return;
                    default:
                        taskExecuter = new executeTaskDelegate(ShowMenu);
                        break;
                }
                taskExecuter();
            }
        }
        private void ShowMenu()
        {
            Console.WriteLine("1. Sa se afiseze toti jucatorii unei echipe dată;");
            Console.WriteLine("2. Sa se afiseze toti jucatorii activi ai unei echipe de la un anumit meci;");
            Console.WriteLine("3. Sa se afiseze toate meciurile dintr-o anumita perioada calendaristica;");
            Console.WriteLine("4. Sa se determine si sa se afiseze scorul de la un anumit meci;");
            Console.WriteLine("Tastati 0 sau exit pentru a iesi.");
        }
        private void GetEchipe()
        {
            List<Echipa<ID>> echipe = service.GetEchipe();
            foreach (Echipa<ID> echipa in echipe)
            {
                Console.WriteLine(echipa);
            }
        }
        private void GetMeciuri()
        {
            List<Meci<ID>> meciuri = service.GetMeciuri();
            foreach (Meci<ID> meci in meciuri)
            {
                Console.WriteLine(meci);
            }
        }
        //1. Sa se afiseze toti jucatorii unei echipe dată
        private void Task1()
        {
            Console.WriteLine("Lista echipelor:");
            GetEchipe();
            Console.Write("Nume echipa pentru care sa afisez toti jucatorii: ");
            string echipa = Console.ReadLine();
            List<Jucator<ID>> jucatoriEchipa = service.GetJucatoriEchipaData(echipa);
            if (jucatoriEchipa.Count > 0)
            {
                foreach (var jucator in jucatoriEchipa)
                {
                    Console.WriteLine(jucator);
                }
            }
            else
                Console.WriteLine("Nu exista!");
            
        }
        //2. Sa se afiseze toti jucatorii activi ai unei echipe de la un anumit meci
        private void Task2()
        {
            Console.WriteLine("Lista meciurilor:");
            GetMeciuri();
            Console.WriteLine("\nLista echipelor:");
            GetEchipe();
            Console.WriteLine("Nume echipa pentru care sa afisez jucatorii activi: ");
            string echipa = Console.ReadLine();
            Console.WriteLine("Activi la meciul cu id meci: ");
            string idMeciString = Console.ReadLine();
            try
            {
                ID idMeci = (ID)Convert.ChangeType(idMeciString, typeof(ID));
                List<JucatorActiv<ID>> jucatoriActiviPtMeci = service.GetJucatoriActiviPeEchipaLaMeci(echipa, idMeci);
                if (jucatoriActiviPtMeci.Count > 0)
                {
                    foreach (var jucator in jucatoriActiviPtMeci)
                    {
                        Console.WriteLine(service.FindJucator(jucator.IdJucator));
                    }
                }
                else
                    Console.WriteLine("Nu exista!");
            }
            catch (Exception) { Console.WriteLine("Date introduse gresit!\n"); }
        }
        //3. Sa se afiseze toate meciurile dintr-o anumita perioada calendaristica
        private void Task3()
        {
            Console.WriteLine("Introdu perioada din care doresti sa vezi meciurile (sub forma ZZ-LL-AAAA / ZZ-LL-AA / ZZ.LL.AAAA / ZZ.LL.AA):");
            Console.WriteLine("Data de inceput: ");
            string data1 = Console.ReadLine();
            Console.WriteLine("Data de sfarsit: ");
            string data2 = Console.ReadLine();
            try{
                List<Meci<ID>> meciuriIntreDate = service.GetMeciuriInPerioada(data1, data2);
                foreach (var meci in meciuriIntreDate)
                {
                    Console.WriteLine(meci);
                }
            }
            catch (Exception) { Console.WriteLine("Data introdusa gresit!"); }
        }
        //4. Sa se determine si sa se afiseze scorul de la un anumit meci
        private void Task4()
        {
            Console.WriteLine("Lista meciurilor:");
            GetMeciuri();
            Console.WriteLine("Id meci pentru care sa afisez scorul: ");
            string idMeciString = Console.ReadLine();
            ID idMeci = (ID)Convert.ChangeType(idMeciString, typeof(ID));
            Console.WriteLine(service.GetScorLaMeci(idMeci));
        }

    }
}
