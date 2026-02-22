function [x_vals, y_vals] = quadraticSpline2(x_data, y_data)
%   QuadraticSpline: Performs quadratic spline interpolation for given data.
%
%   Inputs:
%       x_data: Vector of data points along the x-axis.
%       y_data: Vector of corresponding data points along the y-axis.
%
%   Outputs:
%       x_vals: Vector of x-values for the interpolated spline curve.
%       y_vals: Vector of corresponding y-values for the interpolated spline curve.
%

    % Check input data dimensions
    if length(x_data) ~= length(y_data)
        error('Number of x-data points must match number of y-data points');
    end

    % Number of data points and spline segments
    N = length(x_data) - 1;

    % Build the system of equations
    V = [0; zeros(2 * N, 1); zeros(N - 1, 1)];
    Z = zeros(length(V), length(V));

    % Point matching conditions
    for i = 2:2:2 * N
        Z(i, i:i + 2) = [x_data(i - 1)^2, x_data(i - 1), 1];
        V(i) = y_data(i - 1);
        Z(i + 1, i:i + 2) = [x_data(i)^2, x_data(i), 1];
        V(i + 1) = y_data(i);
    end

    % Smoothing conditions
    for i = 2 * N + 2:3 * N
        Z(i, i:i + 1) = [2 * x_data(i - 1), 1];
        Z(i, i + 3:i + 4) = [-2 * x_data(i), -1];
    end

    % Adjust for "Linear Spline" (zero first derivative at first point)
    Z(1, 1) = 1;

    % Solve for coefficients and interpolate
    Coeff = Z \ V;
    x_vals = [];
    y_vals = [];

    for i = 1:N
        x_vals = [x_vals, x_data(i) : 0.1 : x_data(i + 1)];
        y_vals = [y_vals, Coeff(3 * i - 2) * x_vals(:).^2 + Coeff(3 * i - 1) * x_vals(:) + Coeff(3 * i)];
    end

end
