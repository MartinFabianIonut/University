using System;
using System.Threading;
using System.Net.Sockets;

using ProtobuffProject.services;
using GRPCProject.Protocol;
using Google.Protobuf;
using ProtobuffProject.domain;

namespace ProtobuffNetworking
{
	public class ProtoWorker : IObserver
	{
		private IServices server;
		private TcpClient connection;

		private NetworkStream stream;
		private volatile bool connected;

		public ProtoWorker(IServices server, TcpClient connection)
		{
			this.server = server;
			this.connection = connection;
			try
			{

				stream = connection.GetStream();
				connected = true;
			}
			catch (Exception e)
			{
				Console.WriteLine(e.StackTrace);
			}
		}

		public virtual void Run()
		{
			while (connected)
			{
				try
				{
					Request request = Request.Parser.ParseDelimitedFrom(stream);
					Response response = handleRequest(request);
					if (response != null)
					{
						sendResponse(response);
					}
				}
				catch (Exception e)
				{
					Console.WriteLine(e.StackTrace);
				}

				try
				{
					Thread.Sleep(1000);
				}
				catch (Exception e)
				{
					Console.WriteLine(e.StackTrace);
				}
			}

			try
			{
				stream.Close();
				connection.Close();
			}
			catch (Exception e)
			{
				Console.WriteLine("Error " + e);
			}
		}

		private Response handleRequest(Request request)
		{
			Response response = null;
			Request.Types.Type reqType = request.Type;
			switch (reqType)
			{
				case Request.Types.Type.Login:
				{
					Console.WriteLine("Login request ...");
					Employee<int> employee = ProtoUtils.GetEmployee(request);
					try
					{
						lock (server)
						{
							return ProtoUtils.CreateLoginResponse(
								server.AuthenticateEmployee(employee, this));
						}
					}
					catch (MyException e)
					{
						connected = false;
						return ProtoUtils.CreateErrorResponse(e.Message);
					}
				}
				case Request.Types.Type.Logout:
				{
					Console.WriteLine("Logout request");
					Employee<int> employee = ProtoUtils.GetEmployee(request);
					try
					{
						lock (server)
						{
							server.Logout(employee);
						}

						connected = false;
						return ProtoUtils.CreateOkResponse();

					}
					catch (MyException e)
					{
						return ProtoUtils.CreateErrorResponse(e.Message);
					}
				}
				case Request.Types.Type.BuyTicket:
				{
					Console.WriteLine("Buy ticket request");
					Ticket<int> ticket = ProtoUtils.GetTicket(request);
					try
					{
						lock (server)
						{
							server.AddTickets(ticket);
						}

						return ProtoUtils.CreateOkResponse();
					}
					catch (MyException e)
					{
						return ProtoUtils.CreateErrorResponse(e.Message);
					}
				}
				case Request.Types.Type.GetAllShows:
				{
					Console.WriteLine("Get all shows request");
					try
					{
						lock (server)
						{
							return ProtoUtils.CreateGetAllShowsResponse(server.GetAllShowsDTO());
						}
					}
					catch (MyException e)
					{
						return ProtoUtils.CreateErrorResponse(e.Message);
					}
				}
				case Request.Types.Type.GetAllArtists:
				{
					Console.WriteLine("Get all artists request");
					try
					{
						lock (server)
						{
							return ProtoUtils.CreateGetAllArtistsResponse(server.GetAllArtists());
						}
					}
					catch (MyException e)
					{
						return ProtoUtils.CreateErrorResponse(e.Message);
					}
				}
				case Request.Types.Type.FindArtist:
				{
					Console.WriteLine("Find artist request");
					try
					{
						lock (server)
						{
							return ProtoUtils.CreateFindArtistResponse(
								server.FindArtist(ProtoUtils.GetIdArtist(request)));
						}
					}
					catch (MyException e)
					{
						return ProtoUtils.CreateErrorResponse(e.Message);
					}
				}
			}

			return response;
		}

		private void sendResponse(Response response)
		{
			Console.WriteLine("sending response " + response);
			lock (stream)
			{
				response.WriteDelimitedTo(stream);
				stream.Flush();
			}
		}

		public void TicketAdded(Ticket<int> ticket)
		{
			Console.WriteLine("Ticket added " + ticket);
			try
			{
				sendResponse(ProtoUtils.CreateTicketAddedResponse(ticket));
			}
			catch (Exception e)
			{
				Console.WriteLine(e.StackTrace);
			}
		}
	}

}
