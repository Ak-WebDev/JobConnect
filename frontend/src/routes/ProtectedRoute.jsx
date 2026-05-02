import { Navigate } from "react-router-dom";
import { getRole, getToken } from "../utils/auth";

function ProtectedRoute({ children, allowedRole }) {
  const token = getToken();
  const role = getRole();

  if (!token) {
    return <Navigate to="/" replace />;
  }

  if (allowedRole && role !== allowedRole) {
    return <Navigate to="/" replace />;
  }

  return children;
}

export default ProtectedRoute;