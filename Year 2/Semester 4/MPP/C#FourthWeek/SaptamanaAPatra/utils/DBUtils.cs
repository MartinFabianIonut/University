using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SaptamanaAPatra.utils
{
    public static class DBUtils
    {
        private static IDbConnection instance = null;

        public static IDbConnection GetConnection(IDictionary<string, string> props)
        {
            if (instance != null && instance.State != ConnectionState.Closed) return instance;
            instance = GetNewConnection(props);
            instance.Open();

            return instance;
        }

        private static IDbConnection GetNewConnection(IDictionary<string, string> props)
        {
            return ConnectionFactory.GetInstance().CreateConnection(props);
        }
    }
}
