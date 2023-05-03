using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using ProtobuffProject.services;
using ProtobuffProject.domain;
using GRPCProject.Protocol;
using proto=GRPCProject.Protocol;

namespace ProtobuffNetworking
{
    static class ProtoUtils
    {
        public static Request CreateLoginRequest(Employee<int> employee)
        {
            var myEmployee = new Employee
            {
                Id = employee.Id,
                FirstName = employee.FirstName,
                LastName = employee.LastName,
                Username = employee.Username,
                Password = employee.Password
            };
            return new Request
            {
                Type = Request.Types.Type.Login,
                Employee = myEmployee
            };
        }
        
        public static Request CreateLogoutRequest(Employee<int> employee)
        {
            var myEmployee = new Employee
            {
                Id = employee.Id,
                FirstName = employee.FirstName,
                LastName = employee.LastName,
                Username = employee.Username,
                Password = employee.Password
            };
            return new Request
            {
                Type = Request.Types.Type.Logout,
                Employee = myEmployee
            };
        }
        
        public static Request CreateAddTicketRequest(Ticket<int> ticket)
        {
            Ticket myTicket = new Ticket
            {
                Id = ticket.Id,
                IdShow = ticket.IdShow,
                NameOfCostumer = ticket.NameOfCostumer,
                NoOfSeats = ticket.NoOfSeats
            };
            return new Request
            {
                Type = Request.Types.Type.BuyTicket,
                Ticket = myTicket
            };
        }
        
        public static Request CreateGetAllShowsDTORequest()
        {
            return new Request
            {
                Type = Request.Types.Type.GetAllShows
            };
        }

        public static Request CreateGetAllArtistsRequest()
        {
            return new Request
            {
                Type = Request.Types.Type.GetAllArtists
            };
        }

        public static Request CreateFindArtistRequest(int id)
        {
            return new Request
            {
                Type = Request.Types.Type.FindArtist,
                IdArtist = id
            };
        }
        
        public static string GetError(Response response)
        {
            return response.Error;
        }

        public static Employee<int> GetEmployee(Response response)
        {
            Employee<int> employee = new Employee<int>
            (response.Employee.Id,
                response.Employee.FirstName,
                response.Employee.LastName,
                response.Employee.Username,
                response.Employee.Password);
            return employee;
        }
        
        public static Employee<int> GetEmployee(Request request)
        {
            Employee<int> employee = new Employee<int>
                (request.Employee.Id, 
                request.Employee.FirstName,
                request.Employee.LastName,
                request.Employee.Username,
                request.Employee.Password
            );
            return employee;
        }

        public static Ticket<int> GetTicket(Response response)
        {
            Ticket<int> ticket =
                new Ticket<int>(response.Ticket.Id, 
                    response.Ticket.IdShow, 
                    response.Ticket.NameOfCostumer,
                    response.Ticket.NoOfSeats);
            return ticket;
        }

        public static Ticket<int> GetTicket(Request request)
        {
            Ticket<int> ticket =
                new Ticket<int>(request.Ticket.Id, 
                    request.Ticket.IdShow, 
                    request.Ticket.NameOfCostumer,
                    request.Ticket.NoOfSeats);
            return ticket;
        }
        
        public static List<ShowDTO<int>> GetShows(Response response)
        {
            List<ShowDTO<int>> showDTOList = new List<ShowDTO<int>>();
            foreach (var showDTO in response.ShowDTO)
            {
                showDTOList.Add(new ShowDTO<int>(
                    showDTO.Id, 
                    showDTO.Title, 
                    showDTO.Date, 
                    showDTO.Place, 
                    showDTO.Available, 
                    showDTO.Unavailable, 
                    showDTO.IdArtist));
            }
            return showDTOList;
        }
        
        public static List<Artist<int>> GetArtists(Response response)
        {
            var artistList = new List<Artist<int>>();
            foreach (var artist in response.Artist)
            {
                artistList.Add(new Artist<int>(artist.Id, artist.FirstName, artist.LastName));
            }
            return artistList;
        }

        public static Artist<int> GetArtist(Response response)
        {
            var artist = response.Artist[0];
            return new Artist<int>(artist.Id, artist.FirstName, artist.LastName);
        }

        public static Response CreateErrorResponse(string message)
        {
            return new Response{Type = Response.Types.Type.Error, Error = message};
        }

        public static Response CreateOkResponse()
        {
            return new Response{Type = Response.Types.Type.Ok};
        }
        
        public static Response CreateGetAllShowsResponse(IEnumerable<ShowDTO<int>> shows)
        {
            var showDTOList = new List<ShowDTO>();
            foreach (var showDTO in shows)
            {
                showDTOList.Add(new ShowDTO
                {
                    Id = showDTO.Id,
                    Title = showDTO.Title,
                    Date = showDTO.Date,
                    Place = showDTO.Place,
                    Available = showDTO.Available,
                    Unavailable = showDTO.Unavailable,
                    IdArtist = showDTO.IdArtist
                });
            }
            return new Response{Type = Response.Types.Type.GetAllShows, ShowDTO = {showDTOList}};
        }
        
        public static Response CreateGetAllArtistsResponse(IEnumerable<Artist<int>> artists)
        {
            var artistList = new List<Artist>();
            foreach (var artist in artists)
            {
                artistList.Add(new Artist
                {
                    Id = artist.Id,
                    FirstName = artist.FirstName,
                    LastName = artist.LastName
                });
            }
            return new Response{Type = Response.Types.Type.GetAllArtists, Artist = {artistList}};
        }

        public static Response CreateTicketAddedResponse(Ticket<int> ticket)
        {
            Ticket myTicket = new Ticket
            {
                Id = ticket.Id,
                IdShow = ticket.IdShow,
                NameOfCostumer = ticket.NameOfCostumer,
                NoOfSeats = ticket.NoOfSeats
            };
            return new Response{ Type = Response.Types.Type.TicketSold, Ticket = myTicket};
        }
        
        public static int GetIdArtist(Request request)
        {
            return request.IdArtist;
        }
        
        public static Response CreateFindArtistResponse(Artist<int> artist)
        {
            var myArtist = new Artist
            {
                Id = artist.Id,
                FirstName = artist.FirstName,
                LastName = artist.LastName
            };
            return new Response{Type = Response.Types.Type.FindArtist, Artist = {myArtist}};
        }
        
        public static Response CreateLoginResponse(Employee<int> employee)
        {
            var myEmployee = new Employee
            {
                Id = employee.Id,
                FirstName = employee.FirstName,
                LastName = employee.LastName,
                Username = employee.Username,
                Password = employee.Password
            };
            return new Response{Type = Response.Types.Type.Ok, Employee = myEmployee};
        }
    }
}