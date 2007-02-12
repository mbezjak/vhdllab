library IEEE;
use IEEE.STD_LOGIC_1164.ALL;

entity mux41 is port (
	e : in std_logic;
	d : in std_logic_vector (3 downto 0);
	sel :in std_logic_vector (1 downto 0);
	z :out std_logic);
end mux41;

architecture Behavioral of mux41 is
begin
	process(d,e,sel)
	begin
	
	if (e = '1') then
		case sel is
			when  "00" => z <= d(0);
			when  "01" => z <= d(1);
			when  "10" => z <= d(2);
			when  "11" => z <= d(3);
			when others => z <='0';
		end case;
	else z<='0';
	end if;
	
	end process;
end Behavioral;