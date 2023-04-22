using System;

namespace SeventhWeekCSharpServerClient.client
{
    public enum EmployeeEvent
    {
        TICKET_BOUGHT
    };

    public class EmployeeEventArgs : EventArgs
    {
        public EmployeeEventArgs(EmployeeEvent employeeEvent, object data)
        {
            this.EmployeeEventType = employeeEvent;
            this.Data = data;
        }

        public EmployeeEvent EmployeeEventType { get; }

        public object Data { get; }
    }
}
