import { Link, useNavigate } from "react-router-dom";
import { clearAuthData, getRole, getToken } from "../utils/auth";

function Navbar() {
  const navigate = useNavigate();
  const token = getToken();
  const role = getRole();

  const handleLogout = () => {
    clearAuthData();
    navigate("/");
  };

  return (
    <div style={{ padding: "15px", borderBottom: "1px solid #ccc", marginBottom: "20px" }}>
      {!token ? (
        <>
          <Link to="/" style={{ marginRight: "15px" }}>Login</Link>
          <Link to="/register">Register</Link>
        </>
      ) : (
        <>
          {role === "EMPLOYER" && (
            <Link to="/employer" style={{ marginRight: "15px" }}>Employer Dashboard</Link>
          )}
          {role === "JOB_SEEKER" && (
            <Link to="/seeker" style={{ marginRight: "15px" }}>Seeker Dashboard</Link>
          )}
          <button onClick={handleLogout}>Logout</button>
        </>
      )}
    </div>
  );
}

export default Navbar;