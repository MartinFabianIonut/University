using System;

namespace ProtobuffProject.services
{
    public class MyException : Exception
    {
        public MyException(string message) : base(message)
        {
        }
    }
}
