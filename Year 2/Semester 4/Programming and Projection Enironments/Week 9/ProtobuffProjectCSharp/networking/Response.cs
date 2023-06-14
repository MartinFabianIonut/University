using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ProtobuffProject.networking
{
    public enum ResponseType
    {
        OK, ERROR, UPDATE, TICKET_SOLD, GET_ALL_SHOWS, GET_ALL_ARTISTS, FIND_ARTIST
    }

    [Serializable]
    public class Response
    {
        private Response() { }

        public ResponseType Type { get; private set; }

        public object Data { get; private set; }

        private void SetType(ResponseType type)
        {
            this.Type = type;
        }

        private void SetData(Object data)
        {
            this.Data = data;
        }

        public override string ToString()
        {
            return "Response{" +
                    "type='" + Type + '\'' +
                    ", data='" + Data + '\'' +
                    '}';
        }

        public class Builder
        {
            private readonly Response response = new Response();

            public Builder Type(ResponseType type)
            {
                response.SetType(type);
                return this;
            }

            public Builder Data(Object data)
            {
                response.SetData(data);
                return this;
            }

            public Response Build()
            {
                return response;
            }
        }
    }
}
