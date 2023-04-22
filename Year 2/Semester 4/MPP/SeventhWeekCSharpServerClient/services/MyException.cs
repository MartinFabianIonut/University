using System;

namespace SeventhWeekCSharpServerClient.services
{
    public class MyException : Exception
    {
        public MyException(string message) : base(message)
        {
        }
    }
}
