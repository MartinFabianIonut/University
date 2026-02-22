function [val_f,ni]=Newton_mult(f,df,x0,m,err,NrMaxIt)
 for i=1:NrMaxIt
    x1=x0-m*f(x0)/df(x0);
    if norm(f(x1),Inf)<err||norm(x1-x0,Inf)<err...
      ||norm(x1-x0,Inf)/norm(x1,Inf)<err
      val_f=f(x1);
      ni=i;
      return;
    end
    x0=x1;
  end
  val_f=f(x1);
  ni=NrMaxIt;
end
