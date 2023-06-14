using System;

namespace SeventhWeekCSharpServerClient.networking.utils
{
    public class ServerException : Exception
    {
        public ServerException(string message, Exception innerException) : base(message, innerException)
        {
        }
    }

}
