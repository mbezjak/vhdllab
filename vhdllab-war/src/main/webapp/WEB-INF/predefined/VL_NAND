library ieee;
use ieee.std_logic_1164.all;
 
ENTITY VL_NAND IS 
  GENERIC (
    n: positive := 2;
    delay: time := 10 ns
  );
  PORT (
    a: IN  std_logic_vector(n-1 downto 0);
    f: OUT std_logic
  );
begin
  assert n >= 2;
END VL_NAND;
 
ARCHITECTURE behavioral OF VL_NAND IS
BEGIN
  process(a)
    variable res: std_logic;
  begin
    res := '1';
    for i in n-1 downto 0 loop
      res := res and a(i);
    end loop;
    f <= not res after delay;
  end process;
  
END behavioral;

