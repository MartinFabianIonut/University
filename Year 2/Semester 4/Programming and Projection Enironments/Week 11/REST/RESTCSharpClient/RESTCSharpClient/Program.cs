using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http.Headers;
using System.Net.Http;
using System.Threading;
using System.Threading.Tasks;
using System.Windows.Forms;
using Newtonsoft.Json;
using System.Net.Http.Json;

namespace RESTCSharpClient
{
    class MainClass
    {
        static HttpClient client = new HttpClient(new LoggingHandler(new HttpClientHandler()));

        private static string URL_Base = "http://localhost:8080/music/artists";

        public static void Main(string[] args)
        {
            //Console.WriteLine("Hello World!");
            RunAsync().Wait();
        }


        static async Task RunAsync()
        {
            client.BaseAddress = new Uri(URL_Base);
            client.DefaultRequestHeaders.Accept.Clear();
            client.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("application/json"));

            //Create an artist
            Artist<int> artist = new Artist<int>(2, "Test firstname", "Test lastname");
            Console.WriteLine("Create artist {0}", artist);
            Artist<int> result = await CreateArtistAsync(URL_Base, artist);
            Console.WriteLine("Received: {0}", result);
            artist = result;

            //Update an artist
            artist.FirstName = "Test firstname updated";
            artist.LastName = "Test lastname updated";
            Console.WriteLine("Update artist {0}", artist);
            Artist<int> result1 = await UpdateArtistAsync(URL_Base, artist);
            Console.WriteLine(@"Received: {0}", result1);

            //Get one artist
            Console.WriteLine("Get artist by id {0}", artist.Id);
            Artist<int> result2 = await GetArtistAsync(URL_Base, artist.Id);
            Console.WriteLine("Received: {0}", result2);
            
            // Get all artists
            Console.WriteLine("Get all artists");
            IEnumerable<Artist<int>> artists = await GetArtistsAsync(URL_Base);
            foreach (Artist<int> a in artists)
            {
                Console.WriteLine(a);
            }
            
            // Delete an artist
            Console.WriteLine("Delete artist by id {0}", artist.Id);
            HttpResponseMessage response = await DeleteArtistAsync(URL_Base + "/" + artist.Id);
            Console.WriteLine("Received: {0}", response.StatusCode);
            
            // Get all artists
            Console.WriteLine("Get all artists");
            artists = await GetArtistsAsync(URL_Base);
            foreach (Artist<int> a in artists)
            {
                Console.WriteLine(a);
            }
        }

        private static async Task<IEnumerable<Artist<int>>> GetArtistsAsync(string urlBase)
        {
            IEnumerable<Artist<int>> artists = null;
            HttpResponseMessage response = await client.GetAsync(urlBase);
            if (response.IsSuccessStatusCode)
            {
                artists = await response.Content.ReadAsAsync<IEnumerable<Artist<int>>>();
            }
            return artists;
        }

        private static async Task<HttpResponseMessage> DeleteArtistAsync(string urlBase)
        {
            HttpResponseMessage response = await client.DeleteAsync(urlBase);
            return response;
        }

        private static async Task<Artist<int>> UpdateArtistAsync(string urlBase, Artist<int> artist)
        {
            Artist<int> result = null;
            HttpResponseMessage response = await client.PutAsync(urlBase + "/" + artist.Id, JsonContent.Create(artist));
            if (response.IsSuccessStatusCode)
            {
                result = await response.Content.ReadAsAsync<Artist<int>>();
            }
            return result;
        }

        static async Task<Artist<int>> GetArtistAsync(string path, int id)
        {
            Artist<int> product = null;
            HttpResponseMessage response = await client.GetAsync(path + "/" + id);
            if (response.IsSuccessStatusCode)
            {
                product = await response.Content.ReadAsAsync<Artist<int>>();
            }
            return product;
        }


        static async Task<Artist<int>> CreateArtistAsync(string path, Artist<int> user)
        {
            Artist<int> result = null;
            HttpResponseMessage response = await client.PostAsync(path, JsonContent.Create(user));
            if (response.IsSuccessStatusCode)
            {
                result = await response.Content.ReadAsAsync<Artist<int>>();
            }
            return result;
        }
    }

    public class Entity<ID>
    {
        private ID id;
        private int v;

        public Entity(ID id) { this.id = id; }

        public Entity(int v)
        {
            this.v = v;
        }

        public ID Id
        {
            get => this.id;
            set => this.id = value;
        }
        public override string ToString()
        {
            return id.ToString() + " ";
        }
    }


    public class Artist<ID> : Entity<ID>
    {
        public Artist() : base(0)
        {
            FirstName = "";
            LastName = "";
        }

        public Artist(ID id, string firstName, string lastName) : base(id)
        {
            FirstName = firstName;
            LastName = lastName;
        }

        public string FirstName { get; set; }
        
        public string LastName { get; set; }

        public override string ToString()
        {
            return string.Format("[Artist: Id={0}, FirstName={1}, LastName={2}]", Id, FirstName, LastName);
        }
    }

    public class LoggingHandler : DelegatingHandler
    {
        public LoggingHandler(HttpMessageHandler innerHandler)
            : base(innerHandler)
        {
        }

        protected override async Task<HttpResponseMessage> SendAsync(HttpRequestMessage request, CancellationToken cancellationToken)
        {
            Console.WriteLine("Request:");
            Console.WriteLine(request.ToString());
            if (request.Content != null)
            {
                Console.WriteLine(await request.Content.ReadAsStringAsync());
            }
            Console.WriteLine();

            HttpResponseMessage response = await base.SendAsync(request, cancellationToken);

            Console.WriteLine("Response:");
            Console.WriteLine(response.ToString());
            if (response.Content != null)
            {
                Console.WriteLine(await response.Content.ReadAsStringAsync());
            }
            Console.WriteLine();

            return response;
        }
    }
}
