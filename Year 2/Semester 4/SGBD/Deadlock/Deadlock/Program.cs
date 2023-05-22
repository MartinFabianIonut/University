using System;
using System.Data;
using System.Data.SqlClient;
using System.Threading;

namespace Deadlock
{
    class Program
    {
        static void Deadlock1()
        {
            int stepsUntilAbort = 2;
            while (stepsUntilAbort > 0)
            {
                try
                {
                    string connectionString = "Server=LAPTOP-61RA6802\\SQLEXPRESS;Database=LIBRARIE;Integrated Security=true;TrustServerCertificate=true;";
                    using (var connection = new SqlConnection(connectionString))
                    {
                        using (var command = new SqlCommand("updatesForScriitoriAndPremii", connection))
                        {
                            command.CommandType = CommandType.StoredProcedure;
                            connection.Open();
                            command.ExecuteNonQuery();
                            stepsUntilAbort = -1;
                            Console.WriteLine("Prima tranzactie -> update Scriitori, apoi Premii");
                        }
                    }
                }
                catch (SqlException e)
                {
                    Console.WriteLine(e.Message + " " + e.Number);
                    Console.WriteLine("Prima tranzactie -> EXCEPTIE -> update Scriitori, apoi Premii");
                    Console.WriteLine(stepsUntilAbort);
                    if (e.Number == 1205)
                        stepsUntilAbort--;
                    else
                        break;
                }
            }
            if (stepsUntilAbort == 0)
                Console.WriteLine("Prima tranzactie a fost abandonata!");
            else
                Console.WriteLine("Prima tranzactie finalizata cu succes!");
        }

        static void Deadlock2()
        {
            int stepsUntilAbort = 2;
            while (stepsUntilAbort > 0)
            {
                try
                {
                    string connectionString = "Server=LAPTOP-61RA6802\\SQLEXPRESS;Database=LIBRARIE;Integrated Security=true;TrustServerCertificate=true;";
                    using (var connection = new SqlConnection(connectionString))
                    {
                        using (var command = new SqlCommand("updatesForPremiiAndScriitori", connection))
                        {
                            command.CommandType = CommandType.StoredProcedure;
                            connection.Open();
                            command.ExecuteNonQuery();
                            stepsUntilAbort = -1;
                            Console.WriteLine("A doua tranzactie -> update Premii, apoi Scriitori");
                        }
                    }
                }
                catch (SqlException e)
                {
                    Console.WriteLine(e.Message + " " + e.Number);
                    Console.WriteLine("A doua tranzactie -> EXCEPTIE -> update Premii, apoi Scriitori");
                    Console.WriteLine(stepsUntilAbort);
                    if (e.Number == 1205)
                        stepsUntilAbort--;
                    else
                        break;
                }
            }
            if (stepsUntilAbort == 0)
                Console.WriteLine("A doua tranzactie a fost abandonata!");
            else
                Console.WriteLine("A doua tranzactie finalizata cu succes!");
        }

        static void Main()
        {

            Thread t1 = new Thread(Deadlock1);
            Thread t2 = new Thread(Deadlock2);
            t1.Start();
            t2.Start();
            Console.WriteLine("Final? Acest thread se va executa, cel mai probabil, inainte celor doua care cauzeaza deadlock!");
            Console.ReadKey();
        }
    }
}